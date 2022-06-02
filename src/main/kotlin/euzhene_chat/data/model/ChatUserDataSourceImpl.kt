package euzhene_chat.data.model

import euzhene_chat.data.ChatUserDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase

class ChatUserDataSourceImpl(db: CoroutineDatabase) : ChatUserDataSource {
    private val users = db.getCollection<ChatUser>()
    override suspend fun getAllUsers(): List<ChatUser> {
        return users.find()
            .toList()
    }

    override suspend fun insertUser(user: ChatUser) {
        users.insertOne(user)
    }
}