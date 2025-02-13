package com.example.cs_topics_project_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    val list = listOf('a', 'b', 'c')


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user != null) {
            FirebaseAuth.getInstance().signOut(); //GET RID OF THIS LATER!!
            // go to main activity page (dont have it here)
        }
        startActivity(Intent(this, SignInActivity::class.java))
    }
    fun buttonPressed(view: View){
        val myRandomInt = Random.nextInt(2 )
        findViewById<TextView>(R.id.button).text = list[myRandomInt].toString()
    }
}