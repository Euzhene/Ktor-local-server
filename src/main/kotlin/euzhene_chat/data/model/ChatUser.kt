package euzhene_chat.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable //used when we want to send smt to our server
data class ChatUser(
    @BsonId
    val login: String,
    val password: String,
    val username: String,
)
