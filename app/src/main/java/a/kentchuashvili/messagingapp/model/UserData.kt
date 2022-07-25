package a.kentchuashvili.messagingapp.model

data class UserData(
    var uid: String? = "",
    var username: String? = "",
    var profession: String? = "",
    var conversation: Map<String, List<Message>>? = emptyMap(), //receiver name and message list
)