package a.kentchuashvili.messagingapp.fragments

import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.adapters.ConversationListAdapter
import a.kentchuashvili.messagingapp.model.ConversationListItem
import a.kentchuashvili.messagingapp.model.MessageItem
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ConversationList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fullList: MutableList<ConversationListItem>
    private lateinit var filteredList: MutableList<ConversationListItem>
    private lateinit var search: EditText
    private var recyclerViewAdapter = ConversationListAdapter(mutableListOf())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_conversation_list, container, false)
        recyclerView = view.findViewById(R.id.conversationsRecyclerView)!!
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        search = view.findViewById(R.id.searchEditText)
        search.addTextChangedListener {
            filter(search.text.toString())
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
    }

    private fun fetchData() {
        val displayName = Firebase.auth.currentUser?.displayName

        val messageList = mutableListOf<MessageItem>()
        fullList = mutableListOf()
        filteredList = mutableListOf()

        val messageListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList.clear()
                for (child in dataSnapshot.children) {
                    val value = child.getValue(MessageItem::class.java)
                    if (value?.sender == displayName || value?.receiver == displayName) {
                        messageList.add(value!!)
                    }
                }
                messageList.sortBy {
                    it.timestamp
                }
                val userToLastMassageMap = mutableMapOf<String, MessageItem>()
                messageList.forEach {
                    val otherPersonName =
                        if (it.receiver == displayName) it.sender else it.receiver as String
                    userToLastMassageMap[otherPersonName!!] = it
                }
                fullList = userToLastMassageMap.map {
                    ConversationListItem(
                        username = it.key, lastMessage = it.value.message!!,
                        timestamp = it.value.timestamp!!, null
                    )
                }.toMutableList()
                filter("")
                fullList.forEach {
                    val ref = Firebase.storage.reference.child("profilePictures/${it.username}")
                    ref.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
                        val currProfilePicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        it.picture = currProfilePicture
                        filter("")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        Firebase.database.reference.child("Messages")
            .addValueEventListener(messageListener)
    }

    private fun filter(keyWord: String) {
        if (keyWord == "") {
            filteredList = fullList
            render()
        }

        filteredList = mutableListOf()
        for (i in fullList) {
            if (i.username.contains(keyWord, ignoreCase = true)) {
                filteredList.add(i)
            }
        }
        render()
    }

    private fun render() {
        recyclerViewAdapter.conversationList = filteredList
        recyclerViewAdapter.notifyDataSetChanged()
    }

}