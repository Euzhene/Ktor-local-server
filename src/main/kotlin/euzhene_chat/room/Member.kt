package euzhene_chat.room

import io.ktor.http.cio.websocket.*

data class Member(
    val login:String,
    val username: String,
    val sessionId: String,
    val socket: WebSocketSession
)
