package a.kentchuashvili.messagingapp.fragments

import a.kentchuashvili.messagingapp.R
import a.kentchuashvili.messagingapp.adapters.SearchListAdapter
import a.kentchuashvili.messagingapp.model.SearchListItem
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fullList: MutableList<SearchListItem>
    private lateinit var filteredList: MutableList<SearchListItem>
    private lateinit var search: EditText
    private var recyclerViewAdapter: SearchListAdapter = SearchListAdapter(mutableListOf())

    private val database =
        FirebaseDatabase.getInstance("https://messagingappandroid-default-rtdb.europe-west1.firebasedatabase.app/")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.searchRecyclerView)!!
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        search = view.findViewById(R.id.searchTextEdit)
        search.addTextChangedListener {
            if (search.text.length >= 3 || search.text.isEmpty()) {
                filter(search.text.toString())
            }
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        fullList = mutableListOf()
        val usersRef = database.reference.child("UserDataList")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val value = child.child("uid").value
                    if (Firebase.auth.currentUser!!.uid == value) {
                        continue
                    }
                    val username = child.child("username").value as String
                    val ref = Firebase.storage.reference.child("profilePictures/${username}")
                    ref.getBytes(Long.MAX_VALUE).addOnFailureListener {
                        fullList.add(
                            SearchListItem(
                                username,
                                null,
                                child.child("profession").value as String
                            )
                        )
                        filteredList = fullList
                        render()
                    }.addOnSuccessListener {
                        val currProfilePicture = BitmapFactory.decodeByteArray(it, 0, it.size)
                        fullList.add(
                            SearchListItem(
                                username,
                                currProfilePicture,
                                child.child("profession").value as String
                            )
                        )
                        filteredList = fullList
                        render()
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Cannot connect to database", Toast.LENGTH_LONG).show()
            }
        }
        usersRef.addValueEventListener(postListener)
    }

    private fun filter(keyWord: String) {
        if (keyWord == "") {
            filteredList = fullList
            render()
        }

        filteredList = mutableListOf()
        for (i in fullList) {
            if (i.username!!.contains(keyWord, ignoreCase = true)) {
                filteredList.add(i)
            }
        }
        render()
    }

    private fun render() {
        recyclerViewAdapter.userList = filteredList
        recyclerViewAdapter.notifyDataSetChanged()
    }

}