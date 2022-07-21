package a.kentchuashvili.messagingapp.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class UserAdditionalData(
    @ColumnInfo var profession: String,
    @ColumnInfo var conversation: Map<String, List<Message>>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}