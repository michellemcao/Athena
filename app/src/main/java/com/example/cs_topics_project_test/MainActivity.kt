package com.example.cs_topics_project_test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if (user != null) {
            FirebaseAuth.getInstance().signOut(); //GET RID OF THIS LATER!!
            // go to main activity page (dont have it here)
        //}
        startActivity(Intent(this, SignInActivity::class.java))
    }
}


/*
 - to do maybe: add splash screen before sign in
 - enter first name, last name, & username?? in sign-up page
 - check if email actual exists (verify if email exists with google - prolly some firebase feature)
 - have sign out feature in nav page
 - edit name, email and user id in nav page
 - make sure each has a unique username (store directly to firebase maybe)
 */