package com.example.cs_topics_project_test.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.ui.chat.Person
import com.example.cs_topics_project_test.ui.ui.chat.ChatListAdapter
import androidx.appcompat.widget.Toolbar

class ChatListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarChat)
        setSupportActionBar(toolbar)

        // Set the title of the toolbar
        supportActionBar?.title = "Chat"

        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Assume you have a list of persons (chats)
        val chatList = listOf(
            Person("John Doe", 123),
            Person("Jane Smith", 456)
        )

        // Set up RecyclerView to show the list of persons
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val adapter = ChatListAdapter(chatList) { person ->
            // On item click, navigate to ChatActivity for that person
            val intent = ChatActivity.createIntent(this, person)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Go back when back button is pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

