package a.kentchuashvili.messagingapp.adapters

import a.kentchuashvili.messagingapp.ChatActivity
import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.model.ConversationListItem
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConversationListAdapter(var conversationList: List<ConversationListItem>) :
    RecyclerView.Adapter<ConversationListAdapter.ConversationListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversation_list_item, parent, false)
        return ConversationListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationListViewHolder, position: Int) {
        val item = conversationList[position]
        holder.init(item)
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }

    inner class ConversationListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profilePicture =
            itemView.findViewById<ImageView>(R.id.conversationListItemProfilePicture)
        private val username = itemView.findViewById<TextView>(R.id.conversationListItemUsername)
        private val message = itemView.findViewById<TextView>(R.id.conversationListItemMessage)
        private val timestamp = itemView.findViewById<TextView>(R.id.conversationListItemTimestamp)

        fun init(conversationListItem: ConversationListItem) {

            profilePicture.setImageBitmap(conversationListItem.picture)
            username.text = conversationListItem.username
            message.text = conversationListItem.lastMessage
            timestamp.text = conversationListItem.timestamp

            itemView.setOnClickListener {
                val intent = Intent(it.context, ChatActivity::class.java)
                intent.putExtra("username", username.text)
                it.context.startActivity(intent)
            }
        }
    }
}