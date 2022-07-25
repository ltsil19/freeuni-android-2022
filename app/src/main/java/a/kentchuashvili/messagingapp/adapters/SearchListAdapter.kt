package a.kentchuashvili.messagingapp.adapters

import a.kentchuashvili.messagingapp.ChatActivity
import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.model.SearchListItem
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchListAdapter(var userList: List<SearchListItem>) :
    RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {

    inner class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profilePicture =
            itemView.findViewById<ImageView>(R.id.searchListItemCircleImageView)
        private val username = itemView.findViewById<TextView>(R.id.searchListItemUsername)
        private val profession = itemView.findViewById<TextView>(R.id.searchListItemProfession)

        fun init(searchListItem: SearchListItem) {
            profilePicture.setImageBitmap(searchListItem.picture)
            username.text = searchListItem.username
            profession.text = searchListItem.profession

            itemView.setOnClickListener {
                val intent = Intent(it.context, ChatActivity::class.java)
                intent.putExtra("username", username.text)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_list_item, parent, false)
        return SearchListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        val item = userList[position]
        holder.init(item)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}