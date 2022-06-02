package euzhene_chat.data

import euzhene_chat.data.model.ChatUser
import euzhene_chat.data.model.UserInputData

interface ChatUserDataSource {
    suspend fun getAllUsers():List<ChatUser>
    suspend fun insertUser(user:ChatUser)
    suspend fun getUser(inputData: UserInputData): ChatUser?
}