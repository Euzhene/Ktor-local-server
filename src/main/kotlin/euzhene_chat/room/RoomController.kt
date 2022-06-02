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
        val chatUser = loginOrRegisterUserInDb(userInputData)
        findMemberInDb(
            chatUser.login,
            chatUser.username,
            sessionId,
            socket
        )

    }

    suspend fun sendMessages(login: String, text: String) {
        val currentUser = members[login] ?: throw UserNotExistsException()
        val messageEntity = Message(text, currentUser.username, System.currentTimeMillis())
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

    private fun findMemberInDb(
        login: String,
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(login)) {
            throw MemberAlreadyExistsException()
        }
        members[login] = Member(
            login, username, sessionId, socket
        )
    }

    private suspend fun loginOrRegisterUserInDb(userInputData: UserInputData): ChatUser {
        val user = chatUserDataSource.getUser(userInputData)
        if (user == null) {
            val chatUser = ChatUser(
                userInputData.login,
                userInputData.password,
                //todo throw UserNameNotProvidedException
                userInputData.username!!
            )
            chatUserDataSource.insertUser(chatUser)
        }
        return chatUserDataSource.getUser(userInputData) ?: throw UserNotExistsException()
    }
}