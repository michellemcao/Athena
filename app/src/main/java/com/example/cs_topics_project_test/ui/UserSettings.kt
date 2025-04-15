package com.example.cs_topics_project_test.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.databinding.FragmentUserSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.example.cs_topics_project_test.login.SignInActivity
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserSettings : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentUserSettingsBinding
    private lateinit var firestore: FirebaseFirestore

    // mode is whether fragment is being used for sign up or regular settings
    private var mode: String? = null

    companion object {
        private const val ARG_MODE = "mode"

        fun newInstance(mode: String): UserSettings {
            val fragment = UserSettings()
            val bundle = Bundle()
            bundle.putString(ARG_MODE, mode)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // reads "mode" argument (either "signup" or "settings")
        mode = arguments?.getString(ARG_MODE)
        //mode = arguments?.getString("mode") ?: "settings" // fallback if missing

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserSettingsBinding.inflate(inflater,container,false)
        return binding.root

        // val view = inflater.inflate(R.layout.fragment_user_settings, container, true)
        // return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // text that shows user's name/username
        val prevName = view.findViewById<TextView>(R.id.textView10)
        val prevUsername = view.findViewById<TextView>(R.id.usernameDisplay)

        val user = firebaseAuth.currentUser

        // set text or default sets name and username to none
        prevName.text = user?.displayName?:"No Name"
        prevUsername.text = "No username"

        if (mode == "settings" && user != null) {
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    prevUsername.text = document.getString("username") ?: "No username" // idk if this is necessary but just in case
                }
        }

        // when save button clicked
        binding.submitUserSettings.setOnClickListener {
            // name is the new inputted name, trim to get rid of leading/trailing whitespace
            val name = binding.newName.text.toString().trim()
            val username = binding.username.text.toString().trim()

            if (user != null) {

                var nameUpdated = false
                var usernameUpdated = false

                // if coming from sign in page, must have name and username filled out
                fun checkifDone() {
                    if (mode == "signup" && nameUpdated && usernameUpdated) {
                        startActivity(Intent(requireContext(),SignInActivity::class.java))
                        requireActivity().finish()
                    }
                }


                // if user changed name, change saved name in firestore
                if (name.isNotEmpty() && name != user.displayName) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }
                    user.updateProfile(profileUpdates).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Name updated", Toast.LENGTH_SHORT).show()
                        // variable name kind of confusing here but updates the name shown on screen
                        prevName.text = name
                        nameUpdated = true
                        checkifDone()
                    }
                }

                // same thing for username
                if (username.isNotEmpty()) {
                    // get current username from firestore (check if unchanged)
                    firestore.collection("users").document(user.uid).get()
                        .addOnSuccessListener { doc ->
                            // if username null assign "" to it temporarily
                            val currentUsername = doc.getString("username") ?: ""

                            if (username != currentUsername) {
                                // check if username unique
                                firestore.collection("users").whereEqualTo("username", username).get()
                                    .addOnSuccessListener { docs ->
                                        if (!docs.isEmpty) {
                                            Toast.makeText(
                                                requireContext(),"Username already taken",Toast.LENGTH_SHORT).show()
                                        } else {
                                            val userData = hashMapOf("username" to username)
                                            firestore.collection("users").document(user.uid).set(userData, SetOptions.merge())
                                                .addOnSuccessListener {
                                                    Toast.makeText(requireContext(),"Username updated",Toast.LENGTH_SHORT).show()
                                                    usernameUpdated = true
                                                    prevUsername.text = username
                                                    checkifDone()
                                                }
                                        }
                                    }
                            }
                        }


                }

            }

        }
    }
}





