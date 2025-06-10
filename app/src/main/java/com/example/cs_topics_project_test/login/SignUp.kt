package com.example.cs_topics_project_test.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.example.cs_topics_project_test.ui.UserSettings


class SignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.signInButton.setOnClickListener {
            startActivity(Intent(requireContext(), SignInActivity::class.java))

        }

        // when sign up button clicked
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // send email verification
                            firebaseAuth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener {
                                    Toast.makeText(requireContext(),"Please verify your email address",Toast.LENGTH_SHORT).show()
                                }
                            //added by michelle next 5 lines
                            val userUid = firebaseAuth.currentUser?.uid
                            if (userUid != null) {
                                // After successful signup, add the user to Firestore
                                addUserToFirestore(userUid, email)
                            }

                            val userSettingsFragment = UserSettings.newInstance("signup")
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, userSettingsFragment)
                                .addToBackStack(null)
                                .commit()

                        }
                        else {
                            Toast.makeText(requireContext(), "Sign Up Unsuccessful. ", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Password does not match", Toast.LENGTH_SHORT).show()
            }
            } else {
                Toast.makeText(requireContext(), "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }

        }
    }

    // added by michelle: Method to add the new user's UID to Firestore
    private fun addUserToFirestore(userUid: String, email: String) {
        // Create a map to store additional user data (e.g., email)
        val userData = hashMapOf(
            "uid" to userUid,
            "email" to email,
            "theme" to "cherry"
            // You can add more user data here (e.g., name, profile picture, etc.)
        )

        // Save the user data to Firestore under the "users" collection
        firestore.collection("users")
            .document(userUid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "User added to Firestore", Toast.LENGTH_SHORT).show()
                // Optionally, you can navigate to another activity, e.g., main screen
                //startActivity(Intent(this, SignInActivity::class.java))
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to add user to Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}