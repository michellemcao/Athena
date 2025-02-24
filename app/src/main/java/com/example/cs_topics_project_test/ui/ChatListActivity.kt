package com.example.cs_topics_project_test.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.ui.chat.Person
import com.example.cs_topics_project_test.ui.ui.chat.ChatListAdapter
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.layout.Column
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add


class ChatListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatListAdapter
    private val chatList = mutableListOf<Person>()
    private val db = FirebaseFirestore.getInstance()
    private var chatListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarChat)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Chat"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ChatListAdapter(chatList) { person ->
            val intent = ChatActivity.createIntent(this, person)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load chat list from Firestore
        fetchChats()
    }

    private fun fetchChats() {
        db.collection("chats").get()
            .addOnSuccessListener { snapshots ->
                val newChatList = mutableListOf<Person>()

                // Iterate through each chat document
                for (doc in snapshots.documents) {
                    val chatId = doc.getString("cid") ?: ""
                    val senderId = doc.getString("senderID") ?: ""
                    val recipientId = doc.getString("recipientID") ?: ""

                    if (senderId.isNotBlank() && recipientId.isNotBlank()) {
                        // Fetch sender name first
                        db.collection("users").document(senderId).get()
                            .addOnSuccessListener { senderDoc ->
                                val senderName = senderDoc.getString("name") ?: ""

                                // Now fetch recipient name after sender is fetched
                                db.collection("users").document(recipientId).get()
                                    .addOnSuccessListener { recipientDoc ->
                                        val recipientName = recipientDoc.getString("name") ?: ""

                                        // Create a new Person object and add it to the list
                                        val person = Person("$senderName, $recipientName", chatId)
                                        newChatList.add(person)

                                        // After all documents are processed, update the UI
                                        chatList.clear()
                                        chatList.addAll(newChatList)
                                        adapter.notifyDataSetChanged()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("ChatListActivity", "Error fetching recipient data", e)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e("ChatListActivity", "Error fetching sender data", e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ChatListActivity", "Error fetching chats", e)
            }
    }

/*
    @Composable
    fun CreateChatScreen() {
        val onCreateChat: (String) -> Unit = { recipientUid ->
            // Handle creating a new chat here
            // e.g., navigate to a new chat or add it to the database
            createNewChat(recipientUid)
        }

        CreateChatUI(onCreateChat)
    }

    // The function from your previous code
    @Composable
    fun CreateChatUI(onCreateChat: (String) -> Unit) {
        var recipientUid by remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(16.dp)) {
            // Input field for UID
            TextField(
                value = recipientUid,
                onValueChange = { recipientUid = it },
                label = { Text("Enter recipient UID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Create chat button
            IconButton(onClick = {
                if (recipientUid.isNotEmpty()) {
                    onCreateChat(recipientUid)
                }
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Create new chat")
            }
        }
    }

    // Add logic to create a new chat or open the chat screen
    private fun createNewChat(recipientUid: String) {
        // Logic to create a new chat using Firebase or any other logic
        // E.g., create a chat document in Firebase, associate the recipient ID, etc.
    }
*/

    override fun onDestroy() {
        super.onDestroy()
        chatListener?.remove() // Clean up listener when activity is destroyed
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

