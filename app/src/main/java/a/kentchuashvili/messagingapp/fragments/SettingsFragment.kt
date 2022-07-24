package a.kentchuashvili.messagingapp.fragments

import a.kentchuashvili.messagingapp.MainActivity
import a.kentchuashvili.messagingapp.R
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class SettingsFragment : Fragment() {

    lateinit var profilePicture: ImageView
    lateinit var username: EditText
    lateinit var profession: EditText
    lateinit var updateButton: Button
    lateinit var signOutButton: Button
    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        init(view)
        return view
    }

    fun init(view: View) {
        profilePicture = view.findViewById(R.id.profilePicture)
        username = view.findViewById(R.id.settings_username)
        profession = view.findViewById(R.id.settings_profession)
        updateButton = view.findViewById(R.id.updateButton)
        signOutButton = view.findViewById(R.id.signOutButton)

        val auth = Firebase.auth
        user = auth.currentUser!!

        username.setText(user.displayName)

        val database = FirebaseDatabase.getInstance("https://messagingappandroid-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("UserDataList").child(user.uid).child("profession")

        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                profession.setText(it.result?.value.toString())
            }
        }

        val picRef = Firebase.storage.reference.child("profilePictures/${user.displayName}")
        picRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }

        profilePicture.setOnClickListener {
            uploadImage()
        }

        updateButton.setOnClickListener {
            updateUser()
        }

        signOutButton.setOnClickListener {
            logOut(auth)
        }

    }

    private fun uploadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            profilePicture.setImageURI(data?.data) // handle chosen image
            val imageStorage = Firebase.storage
            val ref = imageStorage.getReference("profilePictures/${user.displayName}")

            if (data != null) {
                ref.putFile(data.data!!)
            }
        }
    }

    private fun updateUser() {

        val newUserName = username.text.toString()

        val update = userProfileChangeRequest {
            displayName = newUserName
        }
        user.updateProfile(update)

        user.updateEmail("$newUserName@test.com")

        val database = FirebaseDatabase.getInstance("https://messagingappandroid-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("UserDataList").child(user.uid)
        ref.child("profession").setValue(profession.text.toString())
        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
    }

    private fun logOut(auth: FirebaseAuth) {
        auth.signOut()
        val intent = Intent(this.context, MainActivity::class.java)
        startActivity(intent)
    }
}