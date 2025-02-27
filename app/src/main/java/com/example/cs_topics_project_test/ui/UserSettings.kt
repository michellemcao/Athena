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


class UserSettings : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentUserSettingsBinding

    val user = firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    // TODO also update username and store somewhere in Firebase (rn it's only name)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_settings, container, true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // name is the new inputted name
        val name = R.id.newName.toString()
        // update display name in firebase
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        val prevName = view.findViewById<TextView>(R.id.textView10)
        if (user != null) {
            prevName.text = user.displayName
        }
        user!!.updateProfile(profileUpdates).addOnCompleteListener { task->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "User profile updated", Toast.LENGTH_SHORT).show()
            }
        }
    }


}