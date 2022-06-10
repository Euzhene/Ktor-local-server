package euzhene_chat.room

import euzhene_chat.data.ChatUserDataSource
import euzhene_chat.data.MessageDataSource
import euzhene_chat.data.model.ChatUser
import euzhene_chat.data.model.Message
import euzhene_chat.data.model.UserInputData
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource,
    private val chatUserDataSource: ChatUserDataSource,
) {
    private val members = ConcurrentHashMap<String, Member>()

    suspend fun onJoin( //add a member to members list to store his data on our server in order to have easy access to them
        userInputData: UserInputData,
        sessionId: String,
        socket: WebSocketSession
    ) {
        val chatUser = (getUserInDb(userInputData))
        addMember(chatUser, sessionId, socket)
    }

    suspend fun register(userInputData: UserInputData): ChatUser {
        return createUserInDb(userInputData)
    }

    suspend fun sendMessages(login: String, text: String) {
        val currentUser = members[login] ?: throw UserNotExistsException()
        val messageEntity = Message(text, currentUser.username)
        //add message to the DB
        messageDataSource.insertMessage(messageEntity)
        val parsedMessage = Json.encodeToString(messageEntity)
        //send message to everyone who is connected to WS
        members.values.forEach { member ->
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getMessages(): List<Message> {
        return messageDataSource.getMessages()
    }

    suspend fun tryDisconnect(login: String) {
        members[login]?.socket?.close()
        if (members.containsKey(login)) {
            members.remove(login)
        }
    }

    private fun addMember(
        chatUser: ChatUser,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(chatUser.login)) {
            throw MemberAlreadyExistsException()
        }
        members[chatUser.login] = Member(chatUser.login, chatUser.username, sessionId, socket)
    }

    suspend fun getUserInDb(userInputData: UserInputData): ChatUser {
        val user = chatUserDataSource.getUser(userInputData.login) ?: throw UserNotExistsException()

        if (user.password != userInputData.password) throw PasswordNotCorrectException()

        return user
    }

    private suspend fun createUserInDb(userInputData: UserInputData): ChatUser {
        val sameUserLogin = chatUserDataSource.canFindUser(userInputData.login)
        if (sameUserLogin) {
            throw LoginIsAlreadyTakenException()
        }

        val chatUser = ChatUser(
            userInputData.login,
            userInputData.password,
            userInputData.username ?: throw UsernameNotProvidedException()
        )
        chatUserDataSource.insertUser(chatUser)
        return chatUserDataSource.getUser(userInputData.login) ?: throw UserNotExistsException()
    }
}