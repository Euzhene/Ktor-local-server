package euzhene_chat.data.model

data class UserInputData(
    val login:String,
    val password:String,
    val username:String? = null //if a client is registering, they must provide a username
)
