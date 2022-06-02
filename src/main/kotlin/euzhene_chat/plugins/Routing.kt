package euzhene_chat.plugins

import euzhene_chat.room.RoomController
import euzhene_chat.route.chatSocket
import euzhene_chat.route.getAllMessages
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() { //When the client makes a request to a specific URL (for example, /hello ), the routing mechanism allows us to define how we want this request to be served.
    val roomController by inject<RoomController>()
    install(Routing){   //means routing handles only chatSocket and getAllMessages requests
        chatSocket(roomController)
        getAllMessages(roomController)
    }
}
