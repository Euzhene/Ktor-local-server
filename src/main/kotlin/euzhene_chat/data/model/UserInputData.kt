package euzhene_chat.data.model

data class UserInputData(
    val login:String,
    val password:String,
    val username:String? //if a client is registering, they must provide a username
    //val email:String,
)
