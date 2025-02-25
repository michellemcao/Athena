package com.example.cs_topics_project_test

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.cs_topics_project_test.login.SignUpActivity


class EmailVerify : Fragment(R.layout.fragment_email_verify){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // when the "return to sign up button" clicked, go to SignInActivity


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_email_verify, container, false)
        // when return to sign up button clicked, go to signup page
        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            startActivity(Intent(requireContext(), SignUpActivity::class.java))
        }
        val signInButton = view.findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
        }
        return view
    }




}