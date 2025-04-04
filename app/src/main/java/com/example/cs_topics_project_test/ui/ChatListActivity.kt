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
        db.collection("chats")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("ChatListActivity", "Error fetching chats", error)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val newChatList = mutableListOf<Chat>() // Use Chat instead of Person
                    val chatDocuments = snapshots.documents

                    Log.d("FetchChats", "Fetched ${chatDocuments.size} chats from Firestore")

                    for (doc in chatDocuments) {
                        val chatId = doc.getString("cid") ?: ""
                        val senderId = doc.getString("senderID")
                        val recipientId = doc.getString("recipientID")

                        // Check if senderID and recipientID are valid
                        if (senderId.isNullOrBlank() || recipientId.isNullOrBlank()) {
                            Log.e("FetchChats", "Invalid chat data: senderID or recipientID missing for chatId: $chatId")
                            continue
                        }

                        Log.d("FetchChats", "Processing chat $chatId: sender=$senderId, recipient=$recipientId")

                        // Fetch sender name
                        db.collection("users").document(senderId).get()
                            .addOnSuccessListener { senderDoc ->
                                val senderName = senderDoc.getString("name") ?: "Unknown Sender"

                                // Fetch recipient name
                                db.collection("users").document(recipientId).get()
                                    .addOnSuccessListener { recipientDoc ->
                                        val recipientName = recipientDoc.getString("name") ?: "Unknown Recipient"

                                        // Create a new Chat object and add it to the list
                                        val chat = Chat(chatId, senderName, recipientName)
                                        newChatList.add(chat)

                                        // Log to verify if newChatList is properly populated
                                        Log.d("FetchChats", "Current chat list size: ${newChatList.size}")

                                        // Update UI after all documents are processed
                                        if (newChatList.size == chatDocuments.size) {
                                            Log.d("FetchChats", "Final chat list size: ${newChatList.size}")
                                            chatList.clear()
                                            chatList.addAll(newChatList)
                                            adapter.notifyDataSetChanged()
                                            Log.d("FetchChats", "Chat list updated successfully")
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("FetchChats", "Error fetching recipient name", e)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e("FetchChats", "Error fetching sender name", e)
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

        val chatDocRef = db.collection("chats").document(chatId)

        chatDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Chat already exists, navigate to it
                val intent = ChatActivity.createIntent(this, Chat(chatId, "Sender", "Recipient"), chatId)
                startActivity(intent)
            } else {
                // Chat does not exist, create a new one
                val chatData = hashMapOf(
                    "cid" to chatId,
                    "senderID" to currentUserUid,
                    "recipientID" to recipientUid,
                    "timestamp" to System.currentTimeMillis()
                )

                chatDocRef.set(chatData)
                    .addOnSuccessListener {
                        // Initialize message collection
                        chatDocRef.collection("messages").add(
                            hashMapOf(
                                "sender" to "system",
                                "message" to "Chat created.",
                                "timestamp" to System.currentTimeMillis()
                            )
                        )

                        // Navigate to the new chat
                        val intent = ChatActivity.createIntent(this, Chat(chatId, "Sender", "Recipient"), chatId)
                        //startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to create chat", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error checking chat existence", Toast.LENGTH_SHORT).show()
        }
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
