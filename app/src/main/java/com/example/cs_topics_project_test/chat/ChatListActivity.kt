package com.example.cs_topics_project_test.chat

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
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import android.util.Log
import android.view.MenuItem
import com.google.firebase.firestore.FieldValue


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
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Get the chat document to examine recipientID
        db.collection("users").document(currentUserId)
            .collection("chats").document(chat.cid)
            .get()
            .addOnSuccessListener { chatDoc ->
                val recipientField = chatDoc.get("recipientID")

                val isGroupChat = when (recipientField) {
                    is List<*> -> recipientField.size > 1
                    else -> false
                }

                if (isGroupChat) {
                    val options = arrayOf("Delete Chat", "Leave Chat")

                    AlertDialog.Builder(this)
                        .setTitle("Group Options")
                        .setItems(options) { _, which ->
                            when (which) {
                                0 -> showDeleteConfirmationDialog(chat)
                                1 -> leaveGroupChat(chat)
                            }
                        }
                        .show()
                } else {
                    // Individual chat
                    isUserBlocked(chat) { isBlocked ->
                        val options = if (isBlocked) {
                            arrayOf("Delete Chat", "Unblock User")
                        } else {
                            arrayOf("Delete Chat", "Block User")
                        }

                        AlertDialog.Builder(this)
                            .setTitle("Options for ${chat.recipientName}")
                            .setItems(options) { _, which ->
                                when (which) {
                                    0 -> showDeleteConfirmationDialog(chat)
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
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load chat options", Toast.LENGTH_SHORT).show()
            }
    }


    private fun leaveGroupChat(chat: Chat) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val chatDocRef = db.collection("chats").document(chat.cid)

        chatDocRef.update("recipientID", FieldValue.arrayRemove(currentUserId))
            .addOnSuccessListener {
                // Remove chat from this user's personal chat list
                db.collection("users").document(currentUserId)
                    .collection("chats").document(chat.cid)
                    .delete()

                Toast.makeText(this, "You left the group chat", Toast.LENGTH_SHORT).show()
                // Update UI to remove chat from list
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to leave chat", Toast.LENGTH_SHORT).show()
            }
    }




    private fun isUserBlocked(chat: Chat, callback: (Boolean) -> Unit) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(false)
        val chatId = chat.cid

        db.collection("users").document(currentUserUid)
            .collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { document ->
                val recipientList = document.get("recipientID") as? List<*>
                val recipientId = recipientList?.firstOrNull { it != currentUserUid } as? String

                if (recipientId != null) {
                    val blockedUserDoc = db.collection("users").document(currentUserUid)
                        .collection("blockedUsers").document(recipientId)

                    blockedUserDoc.get()
                        .addOnSuccessListener { blockedDoc ->
                            callback(blockedDoc.exists())
                        }
                        .addOnFailureListener {
                            callback(false)
                        }
                } else {
                    callback(false) // Couldn't determine recipient
                }
            }
            .addOnFailureListener {
                callback(false)
            }
    }


    private fun unblockUser(chat: Chat) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val chatId = chat.cid  // Use the chat ID to retrieve the recipient ID from Firestore

        db.collection("users").document(currentUserUid)
            .collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { document ->
                val recipientList = document.get("recipientID") as? List<*>
                val recipientId = recipientList?.firstOrNull { it != currentUserUid } as? String

                if (recipientId != null) {
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

        db.collection("users").document(currentUserUid)
            .collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { document ->
                val recipientList = document.get("recipientID") as? List<*>
                val recipientId = recipientList?.firstOrNull { it != currentUserUid } as? String

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
                    val isGroup = doc.getBoolean("isGroup") ?: false

                    val recipientUsername = if (!isGroup) {
                        doc.getString("recipientUsername")
                    } else null


                    val recipientNames: List<String> = when (val nameField = doc.get("recipientName")) {
                        is String -> listOf(nameField) // one-on-one chat
                        is List<*> -> nameField.filterIsInstance<String>() // group chat
                        else -> listOf("Unknown")
                    }

                    // Optional: filter out current user's name
                    val displayName = recipientNames.filter { it != "You" }.joinToString(", ")

                    val senderName = "You" // can also pull from profile if needed
                    val chat = Chat(
                        cid = chatId,
                        recipientName = displayName,
                        recipientUsername = recipientUsername,
                        senderName = senderName,
                        isGroup = isGroup
                    )

                    newChatList.add(chat)

                    // When all chats are processed
                    if (newChatList.size == snapshots.size()) {
                        chatList.clear()
                        chatList.addAll(newChatList)
                        adapter.notifyDataSetChanged()
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

    // look up user by username and return UID
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

                val userNames = mutableMapOf<String, String>()
                val userUsernames = mutableMapOf<String, String>()
                var completedFetches = 0

                // Fetch names and usernames of all participants
                for (uid in allParticipants) {
                    db.collection("users").document(uid).get()
                        .addOnSuccessListener { doc ->
                            val name = doc.getString("name") ?: "User"
                            val username = doc.getString("username") ?: ""
                            userNames[uid] = name
                            userUsernames[uid] = username
                            completedFetches++

                            if (completedFetches == allParticipants.size) {
                                // All user data fetched
                                for (uid in allParticipants) {
                                    val otherParticipantIds = allParticipants.filter { it != uid }
                                    val otherParticipantNames = otherParticipantIds.mapNotNull { userNames[it] }

                                    // For 1-on-1 chat, get recipientUsername
                                    val recipientUsername = if (!isGroup) {
                                        val recipientUid = otherParticipantIds.firstOrNull()
                                        recipientUid?.let { userUsernames[it] }
                                    } else null

                                    // User-specific chat entry
                                    val chatData = hashMapOf(
                                        "recipientID" to otherParticipantIds,
                                        "recipientName" to otherParticipantNames,
                                        "timestamp" to System.currentTimeMillis(),
                                        "chatId" to chatId,
                                        "isGroup" to isGroup
                                    )
                                    if (recipientUsername != null) {
                                        chatData["recipientUsername"] = recipientUsername
                                    }

                                    // Write chat to user's subcollection
                                    db.collection("users").document(uid)
                                        .collection("chats").document(chatId)
                                        .set(chatData)
                                }

                                // Top-level chat document
                                val chatDocData = hashMapOf(
                                    "chatId" to chatId,
                                    "recipientID" to allParticipants,
                                    "recipientName" to allParticipants.map { userNames[it] ?: "User" },
                                    "timestamp" to System.currentTimeMillis(),
                                    "isGroup" to isGroup
                                )
                                if (!isGroup) {
                                    val recipientUid = recipientUids.firstOrNull()
                                    val recipientUsername = recipientUid?.let { userUsernames[it] }
                                    if (recipientUsername != null) {
                                        chatDocData["recipientUsername"] = recipientUsername
                                    }
                                }


                                // Save the main chat doc
                                db.collection("chats").document(chatId).set(chatDocData)

                                // Initial system message
                                db.collection("chats").document(chatId).collection("messages")
                                    .add(
                                        mapOf(
                                            "sender" to "system",
                                            "message" to if (isGroup) "Group chat created" else "Chat started",
                                            "timestamp" to System.currentTimeMillis()
                                        )
                                    )

                                // For ChatActivity display
                                val displayName = allParticipants
                                    .filter { it != currentUserUid }
                                    .mapNotNull { userNames[it] }
                                    .joinToString(", ")

                                val intent = ChatActivity.createIntent(
                                    this,
                                    Chat(chatId, displayName, recipientUsername = if (!isGroup) userUsernames[recipientUids.firstOrNull()] else null, senderName, isGroup),
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
