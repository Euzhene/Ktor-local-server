package euzhene_chat.room

import euzhene_chat.data.MessageDataSource
import euzhene_chat.data.model.Message
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin( //add a member to members list to store his data on our server in order to have easy access to them
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(username)) {
            throw MemberAlreadyExistsException()
        }
        members[username] = Member(
            username, sessionId, socket
        )
    }

    suspend fun sendMessages(username: String, text: String) {
        val messageEntity = Message(text, username, System.currentTimeMillis())
        //add message to the DB
        messageDataSource.insertMessage(messageEntity)
        val parsedMessage = Json.encodeToString(messageEntity)
        //send message to everyone who is connected to WS
        members.values.forEach { member ->
            member.socket.send(Frame.Text(parsedMessage))
        }
    }
    suspend fun getMessages():List<Message> {
        return messageDataSource.getMessages()
    }
    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if (members.containsKey(username)) {
            members.remove(username)
        }
    }
}