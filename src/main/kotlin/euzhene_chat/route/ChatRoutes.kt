package euzhene_chat.route

import euzhene_chat.room.MemberAlreadyExistsException
import euzhene_chat.room.RoomController
import euzhene_chat.session.ChatSession
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController) { //calls every single time a client connects to this route via websockets
    webSocket("/chat-socket") {
        val socket =
            call.sessions.get<ChatSession>()   //get the created session (we could create it here btw).
        if (socket == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            return@webSocket
        }
        try {
            roomController.onJoin(
                userInputData = socket.inputData,
                sessionId = socket.sessionId,
                socket = this
            )
            //something like while(true) i.g. everything below doesn't execute
            incoming.consumeEach {//gets everything from a client's message
                if (it is Frame.Text) {
                    roomController.sendMessages(socket.inputData.login, it.readText())
                }
            }
        } catch (e: MemberAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.tryDisconnect(socket.inputData.login)
        }

    }
}

fun Route.getAllMessages(roomController: RoomController) {
    get("/messages") { //get all the messages from DB and send it to a client
        call.respond(HttpStatusCode.OK, roomController.getMessages())
    }

}