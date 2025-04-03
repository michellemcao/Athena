package com.example.cs_topics_project_test.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs_topics_project_test.HomeActivity
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
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
                             startActivity(Intent(this, HomeActivity::class.java)) }
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
        // TODO check that they have an email in firebase, check if currentuser does this
        binding.forgotpwButton.setOnClickListener {
            // redirect to forgot password page
            setContentView(R.layout.forgot_pw)
            // send password reset email
            findViewById<Button>(R.id.submit_forgotPW).setOnClickListener {
                val email = findViewById<TextView>(R.id.editTextTextEmailAddress).text.toString();
                val user = firebaseAuth.currentUser
                // if email exists send
                if (email.isNotEmpty() && user != null) {
                    // firebase sends verification email
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        if (task.isSuccessful) Toast.makeText(this,"Password reset email sent", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please put your email", Toast.LENGTH_SHORT).show()
                }
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

// TODO maybe use linear layouts to keep centered