package a.kentchuashvili.messagingapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserAdditionalData(
    var profession: String? = "",
    var conversation: Map<String, List<Message>>? = emptyMap() //receiver name and message list
)