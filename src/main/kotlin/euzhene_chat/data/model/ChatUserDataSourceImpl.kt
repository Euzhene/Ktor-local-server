package euzhene_chat.data.model

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import euzhene_chat.data.ChatUserDataSource
import org.litote.kmongo.and
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

    override suspend fun getUser(login: String, password: String): ChatUser? {
        return users.find(
            and(
                eq(ChatUser::login.name, login), eq(ChatUser::password.name, password)
            )
        ).first()
    }
}