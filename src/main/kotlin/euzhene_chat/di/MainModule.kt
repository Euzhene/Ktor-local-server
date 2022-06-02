package euzhene_chat.di

import euzhene_chat.data.ChatUserDataSource
import euzhene_chat.data.ChatUserDataSourceImpl
import euzhene_chat.data.MessageDataSource
import euzhene_chat.data.MessageDataSourceImpl
import euzhene_chat.room.RoomController
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.single
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single(named(MESSAGE_QUALIFIER)) {
        KMongo.createClient()
            .coroutine
            .getDatabase("message_db")
    }
    single(named(CHAT_USER_QUALIFIER)) {
        KMongo.createClient()
            .coroutine
            .getDatabase("chat_user_db")
    }
    single<MessageDataSource>(qualifier = named(MESSAGE_QUALIFIER)) {
        MessageDataSourceImpl(get(qualifier = named(MESSAGE_QUALIFIER)) )
    }
    single<ChatUserDataSource>(qualifier = named(CHAT_USER_QUALIFIER)) {
        ChatUserDataSourceImpl(get(qualifier = named(CHAT_USER_QUALIFIER)))
    }
    single {
        RoomController(
            get(qualifier = named(MESSAGE_QUALIFIER)),
            get(qualifier = named(CHAT_USER_QUALIFIER))
        )
    }
}
private const val MESSAGE_QUALIFIER = "message"
private const val CHAT_USER_QUALIFIER = "chatUser"