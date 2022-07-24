package a.kentchuashvili.messagingapp.adapters

import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.model.ConversationListItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class ConversationListAdapter(var conversationList: List<ConversationListItem>):
    RecyclerView.Adapter<ConversationListAdapter.ConversationListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationListViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false)
        return ConversationListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationListViewHolder, position: Int) {
        val item = conversationList[position]
        holder.init(item)
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }


    inner class ConversationListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profilePicture = itemView.findViewById<ImageView>(R.id.conversationListItemProfilePicture)
        val username = itemView.findViewById<TextView>(R.id.conversationListItemUsername)
        val message = itemView.findViewById<TextView>(R.id.conversationListItemMessage)
        val timestamp = itemView.findViewById<TextView>(R.id.conversationListItemTimestamp)

        fun init(conversationListItem: ConversationListItem){

            profilePicture.setImageBitmap(conversationListItem.picture)
            username.text = conversationListItem.username
            message.text = conversationListItem.lastMessage
            timestamp.text = conversationListItem.timestamp

            itemView.setOnClickListener {
               //TODO go to chat
            }
        }
    }
}