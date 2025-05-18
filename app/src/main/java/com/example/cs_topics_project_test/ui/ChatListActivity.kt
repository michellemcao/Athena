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
import com.google.android.gms.tasks.Tasks


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

        adapter = ChatListAdapter(
            chatList,
            onItemClick = { chat ->
                val chatId = chat.cid
                val intent = ChatActivity.createIntent(this, chat, chatId)
                startActivity(intent)
            },
            onDeleteClick = { chat ->
                // Handle delete action here (e.g., remove from list and notify adapter)
                chatList.remove(chat)
                adapter.notifyDataSetChanged()
            },
            onBlockClick = { chat ->
                // Handle block action here (e.g., show confirmation dialog)
                // You could also flag the user as blocked in your data model
                Toast.makeText(this, "${chat.recipientName} has been blocked", Toast.LENGTH_SHORT)
                    .show()
            },
            onOptionsClick = { chat ->
                showChatOptionsDialog(chat)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load chat list from Firestore
        fetchChats()
    }

    private fun showChatOptionsDialog(chat: Chat) {
        // Call isUserBlocked with a callback to handle async result
        isUserBlocked(chat) { isBlocked ->
            val options = if (isBlocked) {
                arrayOf("Delete Chat", "Unblock User") // Show Unblock option if user is blocked
            } else {
                arrayOf("Delete Chat", "Block User") // Otherwise, show Block option
            }

            AlertDialog.Builder(this)
                .setTitle("Options for ${chat.recipientName}")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> showDeleteConfirmationDialog(chat) // Show confirmation before deleting
                        1 -> {
                            if (isBlocked) {
                                unblockUser(chat)
                            } else {
                                blockUser(chat)
                            }
                        }
                    }
                }
                .show()
        }
    }

    private fun isUserBlocked(chat: Chat, callback: (Boolean) -> Unit) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(false)
        val chatId = chat.cid  // Use the chat ID to retrieve the recipient ID from Firestore

        // Get the recipient ID directly from Firestore
        db.collection("users").document(currentUserUid)
            .collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { document ->
                val recipientId = document.getString("recipientID")
                if (recipientId != null) {
                    // Check if the recipient is blocked
                    val blockedUserDoc = db.collection("users").document(currentUserUid)
                        .collection("blockedUsers").document(recipientId)

                    blockedUserDoc.get().addOnSuccessListener { blockedDoc ->
                        callback(blockedDoc.exists())  // Pass true if blocked, false otherwise
                    }
                } else {
                    callback(false)  // If recipient ID is not found, treat as not blocked
                }
            }
            .addOnFailureListener {
                callback(false)  // If fetch fails, treat as not blocked
            }
    }


    private fun unblockUser(chat: Chat) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val chatId = chat.cid  // Use the chat ID to retrieve the recipient ID from Firestore

        // Get the recipient ID directly from Firestore
        db.collection("users").document(currentUserUid)
            .collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { document ->
                val recipientId = document.getString("recipientID")
                if (recipientId != null) {
                    // Remove the recipient from the blockedUsers collection
                    db.collection("users").document(currentUserUid)
                        .collection("blockedUsers").document(recipientId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "${chat.recipientName} has been unblocked", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to unblock user", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Recipient ID not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch chat info", Toast.LENGTH_SHORT).show()
            }
    }



    private fun showDeleteConfirmationDialog(chat: Chat) {
        AlertDialog.Builder(this)
            .setTitle("Delete Chat")
            .setMessage("Are you sure you want to delete the chat with ${chat.recipientName}? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteChat(chat)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



    private fun deleteChat(chat: Chat) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val chatId = chat.cid

        db.collection("users").document(currentUserUid)
            .collection("chats").document(chatId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Chat deleted", Toast.LENGTH_SHORT).show()

                // Remove from local list and update UI
                chatList.remove(chat)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("ChatListActivity", "Failed to delete chat", e)
                Toast.makeText(this, "Failed to delete chat", Toast.LENGTH_SHORT).show()
            }
    }


    private fun blockUser(chat: Chat) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val chatId = chat.cid

        val blockData = hashMapOf(
            "blocked" to true,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("users").document(currentUserUid)
            .collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { document ->
                val recipientId = document.getString("recipientID")
                if (recipientId != null) {
                    val blockData = hashMapOf(
                        "blocked" to true,
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("users").document(currentUserUid)
                        .collection("blockedUsers").document(recipientId)
                        .set(blockData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "User blocked", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to block user", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Recipient ID not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch chat info", Toast.LENGTH_SHORT).show()
            }
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

                            val chat = Chat(chatId, recipientName, senderName)
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
        builder.setTitle("Enter Usernames (comma-separated)")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Create") { dialog, _ ->
            val usernamesInput = input.text.toString().trim()
            if (usernamesInput.isNotEmpty()) {
                val usernames = usernamesInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                if (usernames.isNotEmpty()) {
                    fetchUidsByUsernames(usernames) { uids ->
                        if (uids.isNotEmpty()) {
                            createNewChat(uids)
                        } else {
                            Toast.makeText(this, "No valid usernames found", Toast.LENGTH_SHORT).show()
                        }
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

    // 🔍 Look up user by username and return UID
    private fun fetchUidsByUsernames(usernames: List<String>, callback: (List<String>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val uids = mutableListOf<String>()
        var processed = 0

        for (username in usernames) {
            db.collection("users")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        uids.add(querySnapshot.documents[0].id)
                    }
                    processed++
                    if (processed == usernames.size) {
                        callback(uids)
                    }
                }
                .addOnFailureListener {
                    processed++
                    if (processed == usernames.size) {
                        callback(uids)
                    }
                }
        }
    }



    private fun createNewChat(recipientUids: List<String>) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val allParticipants = (recipientUids + currentUserUid).sorted()
        val chatId = allParticipants.joinToString("_")
        val isGroup = recipientUids.size > 1

        db.collection("users").document(currentUserUid).get()
            .addOnSuccessListener { userDoc ->
                val senderName = userDoc.getString("name") ?: "You"

                val recipientNames = mutableMapOf<String, String>()
                var completedFetches = 0

                for (recipientUid in recipientUids) {
                    db.collection("users").document(recipientUid).get()
                        .addOnSuccessListener { recipientDoc ->
                            val recipientName = recipientDoc.getString("name") ?: "User"
                            recipientNames[recipientUid] = recipientName

                            // Store chat info for both users
                            val chatData = hashMapOf(
                                "recipientID" to recipientUid,
                                "timestamp" to System.currentTimeMillis(),
                                "chatId" to chatId,
                                "recipientName" to recipientName,
                                "isGroup" to isGroup
                            )
                            val reverseChatData = hashMapOf(
                                "recipientID" to currentUserUid,
                                "timestamp" to System.currentTimeMillis(),
                                "chatId" to chatId,
                                "isGroup" to isGroup
                            )

                            db.collection("users").document(currentUserUid)
                                .collection("chats").document(chatId).set(chatData)

                            db.collection("users").document(recipientUid)
                                .collection("chats").document(chatId).set(reverseChatData)

                            completedFetches++

                            // Only continue once all recipient names have been fetched
                            if (completedFetches == recipientUids.size) {
                                // Build group name if it's a group
                                val displayName = if (isGroup) {
                                    recipientNames.values.joinToString(", ")
                                } else {
                                    recipientNames[recipientUids.first()] ?: "Chat"
                                }

                                // Create initial message
                                db.collection("chats").document(chatId).collection("messages")
                                    .add(
                                        mapOf(
                                            "sender" to "system",
                                            "message" to if (isGroup) "Group chat created" else "Chat started",
                                            "timestamp" to System.currentTimeMillis()
                                        )
                                    )

                                // Start ChatActivity with resolved display name
                                val intent = ChatActivity.createIntent(
                                    this,
                                    Chat(chatId, displayName, senderName),
                                    chatId
                                )
                                startActivity(intent)
                            }
                        }
                }
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
