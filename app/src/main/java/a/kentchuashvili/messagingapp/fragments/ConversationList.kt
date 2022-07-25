package a.kentchuashvili.messagingapp.fragments

import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.adapters.ConversationListAdapter
import a.kentchuashvili.messagingapp.model.ConversationListItem
import a.kentchuashvili.messagingapp.model.Message
import a.kentchuashvili.messagingapp.model.UserData
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.Duration
import java.time.LocalDateTime


class ConversationList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var fullList: MutableList<ConversationListItem>
    private lateinit var filteredList: MutableList<ConversationListItem>
    private lateinit var search: EditText
    var recylerViewAdapter = ConversationListAdapter(mutableListOf())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_conversation_list, container, false)
        recyclerView = view?.findViewById(R.id.conversationsRecyclerView)!!
        recyclerView.adapter = recylerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        search = view.findViewById(R.id.searchEditText)
        search.addTextChangedListener {
            filter(search.text.toString())
        }
        fetchData()
        addOnDataChangedListener()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchData() {
        val database =
            FirebaseDatabase.getInstance("https://messagingappandroid-default-rtdb.europe-west1.firebasedatabase.app/")
        auth = Firebase.auth
        val ref = database.getReference("UserDataList").child(auth.currentUser!!.uid)
        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val res: DataSnapshot? = it.result
                val userData = res!!.getValue(UserData::class.java)
                render(userData!!)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun render(userData: UserData) {
        val conversationMap = userData.conversation ?: HashMap<String, List<Message>>()
        val conversationsList = mutableListOf<ConversationListItem>()
        fullList = mutableListOf()
        for ((key, value) in conversationMap) {
            val lastMessage = value[value.size - 1]
            val ref = Firebase.storage.reference.child("profilePictures/${key}")
            ref.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val currProfilePicture = BitmapFactory.decodeByteArray(it, 0, it.size)
                val conversationListItem = ConversationListItem(
                    key, lastMessage.message!!,
                    convertTimestamp(lastMessage.timestamp!!), currProfilePicture
                )
                conversationsList.add(conversationListItem)
                fullList = conversationsList
                filteredList = conversationsList
                renderFilteredList()
            }
        }
    }


    private fun addOnDataChangedListener() {
        val database =
            FirebaseDatabase.getInstance("https://messagingappandroid-default-rtdb.europe-west1.firebasedatabase.app/")
        auth = Firebase.auth
        val ref = database.getReference("UserDataList").child(auth.currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(UserData::class.java)
                render(value!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Cannot connect to database", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun filter(keyWord: String) {
        if (keyWord == "") {
            filteredList = fullList
            renderFilteredList()
        }

        filteredList = mutableListOf()
        for (i in fullList) {
            if (i.username!!.contains(keyWord, ignoreCase = true)) {
                filteredList.add(i)
            }
        }
        renderFilteredList()
    }

    private fun renderFilteredList() {
        recylerViewAdapter.conversationList = filteredList
        recylerViewAdapter.notifyDataSetChanged()
    }

    //TODO might not work properly
    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertTimestamp(time: String): String {
        Log.i("TIME", time)
        val currentTime = LocalDateTime.now()
        val messageSentOn = LocalDateTime.parse(time)
        val min =
            Duration.between(messageSentOn, currentTime).toMinutes()
        val hr =
            Duration.between(messageSentOn, currentTime).toHours()

        if (hr <= 1)
            return "$min min"
        else if (hr <= 24)
            return "$hr hr"
        else
            return messageSentOn.dayOfMonth.toString() + " " + messageSentOn.month.toString()
    }
}