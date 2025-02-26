package com.example.cs_topics_project_test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cs_topics_project_test.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore //added by michelle

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore //added by michelle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance() //added by michelle

        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))

        }
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            //added by michelle next 5 lines
                            val userUid = firebaseAuth.currentUser?.uid
                            if (userUid != null) {
                                // After successful signup, add the user to Firestore
                                addUserToFirestore(userUid, email)
                            }
                            // redirect to sign in page
                            startActivity(
                                Intent(this, SignInActivity::class.java)
                            )

                        } else { // if sign in fails
                            Toast.makeText(this, "Authentication failed. ", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()

            }
        }
    }

    // added by michelle: Method to add the new user's UID to Firestore
    private fun addUserToFirestore(userUid: String, email: String) {
        // Create a map to store additional user data (e.g., email)
        val userData = hashMapOf(
            "uid" to userUid,
            "email" to email,
            // You can add more user data here (e.g., name, profile picture, etc.)
        )

        // Save the user data to Firestore under the "users" collection
        firestore.collection("users")
            .document(userUid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "User added to Firestore", Toast.LENGTH_SHORT).show()
                // Optionally, you can navigate to another activity, e.g., main screen
                //startActivity(Intent(this, SignInActivity::class.java))
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to add user to Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}