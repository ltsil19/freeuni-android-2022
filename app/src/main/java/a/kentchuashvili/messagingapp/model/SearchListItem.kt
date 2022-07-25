package a.kentchuashvili.messagingapp.model

import android.graphics.Bitmap

data class SearchListItem(
    val username: String? = "",
    val picture: Bitmap?,
    val profession: String? = "",
)
