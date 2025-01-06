package com.example.cs_topics_project_test.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.ui.chat.ChatFragment
import com.example.cs_topics_project_test.ui.chat.Person


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setTitle("Chat")

        // Set up the toolbar and enable the back button
        val toolbar: Toolbar = findViewById(R.id.toolbarChat)
        setSupportActionBar(toolbar)

        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Create a Person object (you can modify the data)
        val person = Person(name = "John Doe", id = 1)

        if (savedInstanceState == null) {
            // Pass the Person object to the ChatFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChatFragment.newInstance(person))
                .commitNow()
        }
    }

    // Handle the back button press in the toolbar
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
