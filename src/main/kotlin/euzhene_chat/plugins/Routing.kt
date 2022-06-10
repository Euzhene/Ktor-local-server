package euzhene_chat.plugins

import euzhene_chat.room.RoomController
import euzhene_chat.route.chatSocket
import euzhene_chat.route.getAllMessages
import euzhene_chat.route.getUserInfo
import euzhene_chat.route.registerUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() { //When the client makes a request to a specific URL (for example, /hello ), the routing mechanism allows us to define how we want this request to be served.
    val roomController by inject<RoomController>()
    install(Routing) {   //means routing handles only chatSocket, getAllMessages,etc. requests
        chatSocket(roomController)
        getAllMessages(roomController)
        getUserInfo(roomController)
        registerUser(roomController)
    }
}
