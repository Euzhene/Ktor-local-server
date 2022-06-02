package euzhene_chat.data

import euzhene_chat.data.model.ChatUser

interface ChatUserDataSource {
    suspend fun getAllUsers():List<ChatUser>
    suspend fun insertUser(user:ChatUser)
}