package com.example.cs_topics_project_test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs_topics_project_test.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.reference.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Navigate to sign-in activity
        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        // Handle sign-up button click
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passET.text.toString().trim()
            val confirmPass = binding.confirmPassEt.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    signUpUser(email, pass)
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to sign up a user
    private fun signUpUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid
                    if (userId != null) {
                        addUserToDatabase(userId, "Alice", email) // Replace "Alice" with a proper name input if applicable
                    }
                    Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()

                    // Redirect to sign-in activity
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Authentication failed."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Function to add a user to the database
    private fun addUserToDatabase(userId: String, name: String, email: String) {
        val user = mapOf(
            "name" to name,
            "email" to email
        )
        usersRef.child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User added to database", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add user to database", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
