package euzhene_chat.data

import euzhene_chat.data.model.Message
import org.litote.kmongo.coroutine.CoroutineDatabase

//work with the local db (MongoDb)
class MessageDataSourceImpl(
    db: CoroutineDatabase
) : MessageDataSource {
    private val messages = db.getCollection<Message>()
    override suspend fun getMessages(): List<Message> {
        return messages.find()
            .descendingSort()
            .toList()
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }

}