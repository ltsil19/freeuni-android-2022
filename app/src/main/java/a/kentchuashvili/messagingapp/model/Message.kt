package a.kentchuashvili.messagingapp.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Message(
    @ColumnInfo var message: String,
    @ColumnInfo var sender: String,
    @ColumnInfo var receiver: String,
    @ColumnInfo var timestamp: String,
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
