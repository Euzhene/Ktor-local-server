package euzhene_chat.route

import euzhene_chat.data.model.UserInputData
import euzhene_chat.room.*
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
            //something like while(true) i.g. everything below that doesn't execute
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

fun Route.getUserInfo(roomController: RoomController) {
    get("/myInfo") {
        try {
            val login = call.parameters["login"]!!
            val password = call.parameters["password"]!!
            val userInputData = UserInputData(login, password)
            call.respond(HttpStatusCode.OK, roomController.getUserInDb(userInputData))

        } catch (e: PasswordNotCorrectException) {
            call.respondText(e.localizedMessage, status = HttpStatusCode.Unauthorized)

        } catch (e: UserNotExistsException) {
            call.respondText(e.localizedMessage, status = HttpStatusCode.Unauthorized)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

fun Route.registerUser(roomController: RoomController) {
    get("/register") {
        try {
            val login = call.parameters["login"]!!
            val password = call.parameters["password"]!!
            val username = call.parameters["username"]!!

            if (username.isBlank() || login.isBlank() || password.isBlank())
                throw ParameterNotFoundException()

            val userInputData = UserInputData(login, password, username)
            call.respond(HttpStatusCode.OK, roomController.register(userInputData))

        } catch (e: LoginIsAlreadyTakenException) {
            call.respondText(e.localizedMessage, status = HttpStatusCode.Conflict)

        } catch (e: ParameterNotFoundException) {
            call.respondText(e.localizedMessage, status = HttpStatusCode.Unauthorized)

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}