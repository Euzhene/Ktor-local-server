package euzhene_chat.data

import euzhene_chat.data.model.ChatUser

interface ChatUserDataSource {
    suspend fun insertUser(user:ChatUser)
    suspend fun getUser(login: String): ChatUser?
    suspend fun canFindUser(login:String):Boolean
}