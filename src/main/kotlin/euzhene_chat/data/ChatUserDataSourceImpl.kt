package euzhene_chat.data

import com.mongodb.client.model.Filters.eq
import euzhene_chat.data.model.ChatUser
import euzhene_chat.data.model.UserInputData
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

    override suspend fun getUser(inputData: UserInputData): ChatUser? {
        return users.find(
            and(
                eq(ChatUser::login.name, inputData.login),
                eq(ChatUser::password.name, inputData.password)
            )
        ).first()
    }
}