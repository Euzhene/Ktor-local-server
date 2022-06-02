package euzhene_chat.session

import euzhene_chat.data.model.UserInputData

data class ChatSession(
    val inputData: UserInputData,
    val sessionId:String,
)
