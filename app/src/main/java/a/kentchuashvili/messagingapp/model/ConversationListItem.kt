package a.kentchuashvili.messagingapp.model

import android.graphics.Bitmap

data class ConversationListItem(
    val username: String,
    val lastMessage: String,
    val timestamp: String,
    var picture: Bitmap?
)
