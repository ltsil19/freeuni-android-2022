package a.kentchuashvili.messagingapp.adapters

import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.model.SearchListItem
import android.util.Log
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
            Log.i("TEST", searchListItem.toString())
            profilePicture.setImageBitmap(searchListItem.picture)
            username.text = searchListItem.username
            profession.text = searchListItem.profession

            itemView.setOnClickListener {
                //TODO go to chat
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        var view =
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