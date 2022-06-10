package euzhene_chat.utils

import kotlinx.serialization.Serializable

@Serializable
data class Resource<T>(
    val data:T?
)