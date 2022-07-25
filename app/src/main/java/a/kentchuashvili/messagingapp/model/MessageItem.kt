package a.kentchuashvili.messagingapp.model

data class MessageItem(
    var message: String? = "",
    var sender: String? = "",
    var receiver: String? = "",
    var timestamp: String? = "",
) {
    override fun toString(): String {
        return "($sender$receiver$timestamp)"
    }
}