package a.kentchuashvili.messagingapp.adapters

import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.model.MessageItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatAdapter(var messageList: List<MessageItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LEFT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false)
            LeftViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item_sender, parent, false)
            RightViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LEFT) {
            val item = messageList[position]
            (holder as LeftViewHolder).init(item)
        } else {
            val item = messageList[position]
            (holder as RightViewHolder).init(item)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val displayName = Firebase.auth.currentUser?.displayName
        return if (messageList[position].sender == displayName) RIGHT else LEFT
    }

    inner class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText = itemView.findViewById<TextView>(R.id.message_send)
        private val timestamp = itemView.findViewById<TextView>(R.id.message_send_time)

        fun init(messageItem: MessageItem) {
            messageText.text = messageItem.message
            timestamp.text = messageItem.timestamp
        }
    }

    inner class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText = itemView.findViewById<TextView>(R.id.message)
        private val timestamp = itemView.findViewById<TextView>(R.id.message_time)

        fun init(messageItem: MessageItem) {
            messageText.text = messageItem.message
            timestamp.text = messageItem.timestamp
        }
    }

    companion object {
        const val LEFT = 0
        const val RIGHT = 1
    }

}
