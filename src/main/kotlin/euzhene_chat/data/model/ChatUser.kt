package euzhene_chat.data.model

import org.bson.codecs.pojo.annotations.BsonId

data class ChatUser(
    @BsonId
    val login: String,
    val password: String,
    val username: String,
)
