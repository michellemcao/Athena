package com.example.cs_topics_project_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user != null) {
            FirebaseAuth.getInstance().signOut(); //GET RID OF THIS LATER!!
            // go to main activity page (dont have it here)
        }
        startActivity(Intent(this, SignInActivity::class.java))
    }
    private val list :MutableList<String> = mutableListOf("What do you call a factory that makes okay products? ",
        "How do you organize a space party?",
        "Where do pirates buy their hooks?",
        "What did the janitor say when he jumped out of the closet?",
        "What did the Buffalo say to his little boy when he left the house?",
        "Why is so great about Switzerland?",
        "Why did the pony need a drink of water?",
        "What do you call a pig who knows how to use a knife?")
    fun onClick(view: View){
        val myRandomInt = Random.nextInt(list.size)
        val t = findViewById<TextView>(R.id.textView6)
        var b = findViewById<Button>(R.id.button)
        t.text = list[myRandomInt].toString()

    }


}