package a.kentchuashvili.messagingapp

import a.kentchuashvili.messagingapp.model.UserAdditionalData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var nickname: EditText
    lateinit var password: EditText
    lateinit var profession: EditText
    lateinit var signIn: Button
    lateinit var loginPageSignUp: Button
    lateinit var registerPageSignUp: Button
    lateinit var unregistered: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        nickname = findViewById(R.id.logInNickname)
        password = findViewById(R.id.logInPassword)
        profession = findViewById(R.id.whatIdoTextField)
        signIn = findViewById(R.id.signInButton)
        loginPageSignUp = findViewById(R.id.loginSignUpButton)
        registerPageSignUp = findViewById(R.id.registerSignUpButton)
        unregistered = findViewById(R.id.notRegisteredTextView)

        auth = Firebase.auth
        if (auth.currentUser != null) {
            Log.i("AUTH", "user is saved")
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            return
        }
        signIn.setOnClickListener {
            login()
        }
        loginPageSignUp.setOnClickListener {
            makeRegisterPage()
        }
        registerPageSignUp.setOnClickListener {
            register()
        }
    }

    private fun login() {
        val userName = nickname.text.toString()
        val email = "$userName@test.com"
        val password = password.text.toString();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
        { task ->
            if (task.isSuccessful) {
                Log.i("debug", "is success")
                Toast.makeText(baseContext, "Success", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
            } else {
                Log.i("debug", "is fail")
                Toast.makeText(baseContext, "Failure", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun makeRegisterPage() {
        unregistered.visibility = View.GONE
        loginPageSignUp.visibility = View.GONE
        signIn.visibility = View.GONE
        profession.visibility = View.VISIBLE

        registerPageSignUp.visibility = View.VISIBLE
    }

    private fun register() {
        val userName = nickname.text.toString()
        val email = "$userName@test.com"
        Log.i("debug", email)
        val password = password.text.toString();
        val profession = profession.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    var user = auth.currentUser
                    val database = FirebaseDatabase.getInstance()
                    val newUserReference = database.getReference("UserDataList")
                    val profileUpdates = userProfileChangeRequest {
                        displayName = userName
                    }
                    user!!.updateProfile(profileUpdates)
                    newUserReference.child(user!!.uid)
                        .setValue(UserAdditionalData(profession, emptyMap()))
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}