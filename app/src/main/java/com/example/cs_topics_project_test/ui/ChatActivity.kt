package com.example.cs_topics_project_test.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.ui.ui.chat.MessageAdapter
import com.example.cs_topics_project_test.ui.ui.chat.Chat
import com.example.cs_topics_project_test.ui.ui.chat.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatId: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var chatListener: ListenerRegistration? = null

    companion object {
        private const val EXTRA_CHAT = "extra_chat"

        fun createIntent(context: Context, chat: Chat, chatId: String): Intent {
            return Intent(context, ChatActivity::class.java)
                .putExtra(EXTRA_CHAT, chat)
                .putExtra("CHAT_ID", chatId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chat: Chat = intent.getParcelableExtra(EXTRA_CHAT)
            ?: throw IllegalArgumentException("Chat must be provided")

        title = chat.recipientName // Assuming `Chat` has a `senderName` property
        val toolbar: Toolbar = findViewById(R.id.toolbarChat)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter

        editTextMessage = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.sendButton)

        chatId = intent.getStringExtra("CHAT_ID")
            ?: throw IllegalArgumentException("Chat ID must be provided")
        listenForMessages()

        sendButton.setOnClickListener { sendMessage() }
    }

    private fun generateChatId(recipientId: String): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("User must be logged in")

        val currentUserId = currentUser.uid

        // Generate a consistent chatId using a lexicographical order (sort ids alphabetically)
        val sortedIds = listOf(currentUserId, recipientId).sorted()
        return sortedIds.joinToString("_")
    }

    private fun sendMessage() {
        val messageText = editTextMessage.text.toString()
        if (messageText.isEmpty()) return

        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val currentUserId = currentUser.uid

        db.collection("users").document(currentUserId)
            .collection("chats").document(chatId)
            .get()
            .addOnSuccessListener { chatDoc ->
                val recipientIdField = chatDoc.get("recipientID")
                val recipientIds: List<String> = when (recipientIdField) {
                    is String -> listOf(recipientIdField)
                    is List<*> -> recipientIdField.filterIsInstance<String>()
                    else -> {
                        Log.e("ChatActivity", "Invalid recipientID type")
                        return@addOnSuccessListener
                    }
                }

                if (recipientIds.isEmpty()) {
                    Log.e("ChatActivity", "No valid recipients found")
                    return@addOnSuccessListener
                }

                // Track completion and blocking
                var checksDone = 0
                var isBlocked = false

                for (recipientId in recipientIds) {
                    // Check if current user blocked recipient
                    db.collection("users").document(currentUserId)
                        .collection("blockedUsers").document(recipientId)
                        .get()
                        .addOnSuccessListener { blockedDoc ->
                            if (blockedDoc.exists()) {
                                Toast.makeText(this, "You have blocked $recipientId", Toast.LENGTH_SHORT).show()
                                isBlocked = true
                            }

                            // Check if recipient blocked current user
                            db.collection("users").document(recipientId)
                                .collection("blockedUsers").document(currentUserId)
                                .get()
                                .addOnSuccessListener { blockedByRecipientDoc ->
                                    if (blockedByRecipientDoc.exists()) {
                                        db.collection("users").document(recipientId)
                                            .get()
                                            .addOnSuccessListener { recipientDoc ->
                                                val recipientName = recipientDoc.getString("name") ?: "This user"
                                                Toast.makeText(this, "$recipientName has blocked you", Toast.LENGTH_SHORT).show()
                                                isBlocked = true
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this, "You cannot message this user", Toast.LENGTH_SHORT).show()
                                                isBlocked = true
                                            }
                                    }

                                    // After checking all recipients
                                    checksDone++
                                    if (checksDone == recipientIds.size && !isBlocked) {

                                        val isGroupChat = recipientIds.size > 1 // true if more than one recipient

                                        val newMessage = hashMapOf(
                                            "senderId" to currentUserId,
                                            "text" to messageText,
                                            "timestamp" to System.currentTimeMillis()
                                        )

                                        db.collection("chats").document(chatId)
                                            .collection("messages")
                                            .add(newMessage)
                                            .addOnSuccessListener {
                                                editTextMessage.setText("")
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e("ChatActivity", "Error sending message", exception)
                                            }

                                        if (isGroupChat) {
                                            val senderName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Unknown"
                                            newMessage["senderName"] = senderName
                                        }
                                    }

                                }
                        }
                }
            }
    }



    private val senderNameCache = mutableMapOf<String, String>()

    private fun listenForMessages() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val currentUserId = currentUser.uid

        db.collection("users").document(currentUserId)
            .collection("blockedUsers")
            .get()
            .addOnSuccessListener { blockedSnapshot ->
                val blockedUserIds = blockedSnapshot.documents.mapNotNull { it.id }

                chatListener = db.collection("chats").document(chatId)
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener { snapshots, e ->
                        if (e != null || snapshots == null) return@addSnapshotListener

                        val documents = snapshots.documents
                        if (documents.isEmpty()) {
                            messageList.clear()
                            messageAdapter.notifyDataSetChanged()
                            return@addSnapshotListener
                        }

                        val messagesToAdd = mutableListOf<Message>()
                        var pendingFetches = 0
                        var completedFetches = 0

                        documents.forEach { doc ->
                            val senderId = doc.getString("senderId") ?: return@forEach
                            val text = doc.getString("text") ?: return@forEach
                            val timestamp = doc.getLong("timestamp")?.toString() ?: return@forEach

                            if (blockedUserIds.contains(senderId)) return@forEach

                            val messageType = if (senderId == currentUserId)
                                Message.MessageType.SENT else Message.MessageType.RECEIVED

                            val cachedName = senderNameCache[senderId]
                            if (cachedName != null) {
                                messagesToAdd.add(
                                    Message(senderId, cachedName, text, timestamp, messageType)
                                )
                            } else {
                                pendingFetches++
                                db.collection("users").document(senderId).get()
                                    .addOnSuccessListener { userDoc ->
                                        val senderName = userDoc.getString("name") ?: "Unknown"
                                        senderNameCache[senderId] = senderName

                                        messagesToAdd.add(
                                            Message(senderId, senderName, text, timestamp, messageType)
                                        )

                                        completedFetches++
                                        if (completedFetches == pendingFetches) {
                                            updateMessages(messagesToAdd)
                                        }
                                    }
                                    .addOnFailureListener {
                                        senderNameCache[senderId] = "Unknown"

                                        messagesToAdd.add(
                                            Message(senderId, "Unknown", text, timestamp, messageType)
                                        )

                                        completedFetches++
                                        if (completedFetches == pendingFetches) {
                                            updateMessages(messagesToAdd)
                                        }
                                    }
                            }
                        }

                        // If no fetches needed, update immediately
                        if (pendingFetches == 0) {
                            updateMessages(messagesToAdd)
                        }
                    }
            }
    }

    private fun updateMessages(messages: List<Message>) {
        messageList.clear()
        messageList.addAll(messages.sortedBy { it.timestamp.toLong() })
        messageAdapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(messageList.size - 1)
    }


    override fun onDestroy() {
        super.onDestroy()
        chatListener?.remove()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
