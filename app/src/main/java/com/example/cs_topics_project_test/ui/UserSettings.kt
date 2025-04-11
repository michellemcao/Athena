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

        firestore = FirebaseFirestore.getInstance()

    }
    // TODO also update username and store somewhere in Firebase (rn it's only name)

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

        // when submit button clicked
        binding.submitUserSettings.setOnClickListener {
            // name is the new inputted name
            val name = binding.newName.text.toString()
            val username = binding.username.text.toString()
            val user = firebaseAuth.currentUser

            if (user != null) {

                // update display name in firebase
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                val prevName = view.findViewById<TextView>(R.id.textView10)
                prevName.text = user.displayName

                user.updateProfile(profileUpdates).addOnCompleteListener { task->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "User profile updated", Toast.LENGTH_SHORT).show()

                        firestore.collection("users")
                            // check if username unique
                            .whereEqualTo("username",username).get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    Toast.makeText(requireContext(), "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show()
                                } else {
                                    user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // add username to firestore
                                            val userData = hashMapOf("username" to username)

                                            firestore.collection("users").document(user.uid).set(userData)
                                                .addOnSuccessListener {
                                                    Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                                                    // if user came from the sign up screen, go back to sign in
                                                    if (mode == "signup") {
                                                        startActivity(Intent(requireContext(), SignInActivity::class.java))
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



    }


}