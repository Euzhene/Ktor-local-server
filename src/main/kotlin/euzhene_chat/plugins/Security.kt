package euzhene_chat.plugins


import euzhene_chat.room.Member
import euzhene_chat.session.ChatSession
import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<ChatSession>("SESSION")  //create a session with a specific name (you can use any name and sessions)
            // cookie<Member>("SESSION_1")
    }

    intercept(ApplicationCallPipeline.Features) {   //calls when a client connects to our server to create a session for them (handle a client's request)
        //call - respond on an url a client sends to server
        if (call.sessions.get<ChatSession>() == null) {
            val username = call.parameters["username"] ?: "guest"
            call.sessions.set(ChatSession(username, generateNonce()))   //register a session for one client
        }
    }
}
