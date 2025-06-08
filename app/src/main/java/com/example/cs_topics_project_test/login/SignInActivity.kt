package com.example.cs_topics_project_test.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.cs_topics_project_test.HomeActivity
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.databinding.ActivitySignInBinding
import com.example.cs_topics_project_test.task.TaskDataStructure
import com.example.cs_topics_project_test.task.TaskManager
import com.example.cs_topics_project_test.themes.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import org.w3c.dom.Text

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // shows activity_sign_in.xml page
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // when signUpButton clicked, goes to SignUpActivity page
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }

       binding.signInButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null && user.isEmailVerified) { // goes to home activity if user successfully sign in and verified
                            Toast.makeText(this, "Sign In Successful. ", Toast.LENGTH_SHORT).show()
                            TaskDataStructure.init()
                            TaskDataStructure.initializeDatabase()
                            // TaskManager.init()
                            // retrieve user color scheme here
                            val themeName = ThemeManager.currentThemeName // default mango
                            ThemeManager.loadTheme(this, themeName) // load once for the whole app
                            Toast.makeText(this, "Theme loaded!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeActivity::class.java))
                            /*
                            //  if sign in successful, go to page to put name/username, store to firebase
                            setContentView(R.layout.signin_userinfo)
                            findViewById<Button>(R.id.userinfo_button).setOnClickListener {
                                // if all fields are filled out
                                if (findViewById<TextView>(R.id.firstname).text.toString().isNotEmpty() && findViewById<TextView>(R.id.lastname).text.toString().isNotEmpty() && findViewById<TextView>(R.id.setusername).text.toString().isNotEmpty()) {
                                    val profileUpdates = userProfileChangeRequest {
                                        displayName = findViewById<TextView>(R.id.firstname).text.toString()
                                        //  add username
                                    }
                                    user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Account Details Set", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, HomeActivity::class.java))
                                        }
                                    }
                                     }
                                }*/
                            }
                        else {
                            // if user not verified
                            Toast.makeText(this, "Account not yet verified", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // if sign in fails, display message to user
                        Toast.makeText(this, "Authentication failed. ", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show()
            }
        }

        // forgot password
        binding.forgotpwButton.setOnClickListener {
            // redirect to forgot password page
            setContentView(R.layout.forgot_pw)

            // send password reset email
            findViewById<Button>(R.id.submit_forgotPW).setOnClickListener {
                val email = findViewById<TextView>(R.id.editTextTextEmailAddress).text.toString().trim()

                // if email exists send
                if (email.isNotEmpty()) {
                    // firebase sends verification email
                    firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                        Toast.makeText(this,"Password reset email sent", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                } else {
                    Toast.makeText(this, "Please put your email", Toast.LENGTH_SHORT).show()
                }
            }
            findViewById<Button>(R.id.backButton).setOnClickListener {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
    }

   override fun onStart() {
        super.onStart()

       // check if user is signed in (not null), should go to main part of app (i don't have that so rn just stuck at sign in page)
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}