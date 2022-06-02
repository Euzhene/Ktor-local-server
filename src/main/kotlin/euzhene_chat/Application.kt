package euzhene_chat

import euzhene_chat.plugins.configureMonitoring
import euzhene_chat.di.mainModule
import euzhene_chat.plugins.configureRouting
import euzhene_chat.plugins.configureSecurity
import euzhene_chat.plugins.configureSerialization
import euzhene_chat.plugins.configureSockets


import io.ktor.application.*
import org.koin.ktor.ext.Koin


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)  //launch the engine for our server

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {     //configure koin
        modules(mainModule)
    }
    configureSockets()
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureMonitoring()
}
