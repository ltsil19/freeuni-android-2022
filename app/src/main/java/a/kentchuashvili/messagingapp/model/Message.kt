package a.kentchuashvili.messagingapp.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Message(
     var message: String? = "",
     var sender: String? = "",
     var receiver: String? = "",
     var timestamp: String? = "",
)
