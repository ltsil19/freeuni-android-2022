package a.kentchuashvili.messagingapp

import a.kentchuashvili.messagingapp.adapters.ChatAdapter
import a.kentchuashvili.messagingapp.model.MessageItem
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var topText: TextView
    private lateinit var messageEdit: EditText
    private lateinit var backBtn: ImageButton
    private lateinit var sendBtn: ImageButton
    private lateinit var messageList: MutableList<MessageItem>
    private lateinit var recyclerView: RecyclerView
    private lateinit var receiverName: String
    private lateinit var userName: String

    private val database = Firebase.database
    private var recyclerViewAdapter: ChatAdapter = ChatAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        topText = findViewById(R.id.receiverNameAndProfession)

        recyclerView = findViewById(R.id.chatRecyclerview)!!
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        messageEdit = findViewById(R.id.messageText)
        sendBtn = findViewById(R.id.sendBtn_1)
        sendBtn.setOnClickListener {
            sendMessage()
            messageEdit.setText("")
        }
        backBtn = findViewById(R.id.backButton)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        userName = Firebase.auth.currentUser?.displayName!!
        receiverName = intent.getStringExtra("username")!!
        topText.text = receiverName

        refreshMessages()
    }

    private fun refreshMessages() {
        messageList = mutableListOf()
        val messageListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList.clear()
                for (child in dataSnapshot.children) {
                    val value = child.getValue(MessageItem::class.java)
                    if ((value?.receiver == userName && value.sender == receiverName)
                        || (value?.receiver == receiverName && value.sender == userName)
                    ) {
                        messageList.add(value)
                    }
                }
                messageList.sortBy {
                    it.timestamp
                }
                recyclerViewAdapter.messageList = messageList
                recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        database.reference.child("Messages")
            .addValueEventListener(messageListener)
    }

    private fun sendMessage() {
        if (messageEdit.text.toString().isNotBlank()) {
            val message =
                MessageItem(
                    messageEdit.text.toString(),
                    userName,
                    receiverName,
                    Date().toString()
                )
            database.reference.child("Messages").child(message.toString()).setValue(message)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}