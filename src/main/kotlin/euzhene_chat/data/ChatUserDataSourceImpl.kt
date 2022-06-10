package euzhene_chat.data

import com.mongodb.client.model.Filters.eq
import euzhene_chat.data.model.ChatUser
import org.litote.kmongo.coroutine.CoroutineDatabase

class ChatUserDataSourceImpl(db: CoroutineDatabase) : ChatUserDataSource {
    private val users = db.getCollection<ChatUser>()

    override suspend fun insertUser(user: ChatUser) {
        users.insertOne(user)
    }

    override suspend fun getUser(login: String): ChatUser? {
        return users.find(eq("_id", login)).first()
    }

    override suspend fun canFindUser(login: String): Boolean {
        return getUser(login) != null
    }

}