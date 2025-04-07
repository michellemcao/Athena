package com.example.cs_topics_project_test.ui

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.ui.ui.chat.ChatListAdapter
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import android.util.Log
import android.view.MenuItem
import com.example.cs_topics_project_test.ui.ui.chat.Chat

class ChatListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatListAdapter
    private val chatList = mutableListOf<Chat>() // Use Chat instead of Person
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

        val createChatButton: ImageButton = findViewById(R.id.createChatButton)
        createChatButton.setOnClickListener {
            showCreateChatDialog()
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ChatListAdapter(chatList) { chat ->
            val chatId = chat.cid // Access chatId from the Chat object
            val intent = ChatActivity.createIntent(this, chat, chatId)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load chat list from Firestore
        fetchChats()
    }

    private fun fetchChats() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userChatsRef = db.collection("users").document(currentUserUid).collection("chats")

        userChatsRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.e("ChatListActivity", "Error fetching user chats", error)
                return@addSnapshotListener
            }

            if (snapshots != null) {
                val newChatList = mutableListOf<Chat>()

                for (doc in snapshots.documents) {
                    val chatId = doc.id
                    val recipientId = doc.getString("recipientID") ?: continue

                    db.collection("users").document(recipientId).get()
                        .addOnSuccessListener { recipientDoc ->
                            val recipientName = recipientDoc.getString("name") ?: "Unknown"
                            val senderName = "You" // Optional: or pull from current user profile

                            val chat = Chat(chatId, senderName, recipientName)
                            newChatList.add(chat)

                            if (newChatList.size == snapshots.size()) {
                                chatList.clear()
                                chatList.addAll(newChatList)
                                adapter.notifyDataSetChanged()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("ChatListActivity", "Failed to get recipient info", e)
                        }
                }
            }
        }
    }


    private fun showCreateChatDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Recipient UID")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Create") { dialog, _ ->
            val recipientUid = input.text.toString()
            if (recipientUid.isNotEmpty()) {
                // First check if recipient UID exists in Firebase
                checkRecipientUidExists(recipientUid) { exists ->
                    if (exists) {
                        createNewChat(recipientUid)
                    } else {
                        Toast.makeText(this, "Recipient UID not found!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun checkRecipientUidExists(recipientUid: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")  // Assuming you store users in a "users" collection
            .document(recipientUid)
            .get()
            .addOnSuccessListener { document ->
                // Check if the document exists
                callback(document.exists())
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun createNewChat(recipientUid: String) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val chatId = listOf(currentUserUid, recipientUid).sorted().joinToString("_")

        val chatData = hashMapOf(
            "recipientID" to recipientUid,
            "timestamp" to System.currentTimeMillis(),
            "chatId" to chatId // ✅ Add chatId field
        )

        val reverseChatData = hashMapOf(
            "recipientID" to currentUserUid,
            "timestamp" to System.currentTimeMillis(),
            "chatId" to chatId // ✅ Add chatId field
        )

        val userChatsRef = db.collection("users").document(currentUserUid).collection("chats").document(chatId)
        val recipientChatsRef = db.collection("users").document(recipientUid).collection("chats").document(chatId)

        userChatsRef.set(chatData)
        recipientChatsRef.set(reverseChatData)

        // Initialize the chat in the main 'chats' collection (optional)
        db.collection("chats").document(chatId).collection("messages")
            .add(
                mapOf(
                    "sender" to "system",
                    "message" to "Chat started",
                    "timestamp" to System.currentTimeMillis()
                )
            )

        // Navigate to chat screen
        val intent = ChatActivity.createIntent(this, Chat(chatId, "You", "Recipient"), chatId)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        chatListener?.remove() // Clean up listener when activity is destroyed
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
