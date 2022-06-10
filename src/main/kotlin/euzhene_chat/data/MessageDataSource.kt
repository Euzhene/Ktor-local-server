package euzhene_chat.data

import euzhene_chat.data.model.Message

interface MessageDataSource {
    suspend fun getMessages():List<Message>
    suspend fun insertMessage(message: Message)

}