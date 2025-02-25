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
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.ui.ui.chat.MessageAdapter
import com.example.cs_topics_project_test.ui.chat.Person
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
        private const val EXTRA_PERSON = "extra_person"

        fun createIntent(context: Context, person: Person): Intent {
            return Intent(context, ChatActivity::class.java).putExtra(EXTRA_PERSON, person)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val person: Person = intent.getParcelableExtra(EXTRA_PERSON)
            ?: throw IllegalArgumentException("Person must be provided")

        title = person.name
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

        val currentUser = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("User must be logged in")

        val newMessage = hashMapOf(
            "senderId" to currentUser.uid,
            "text" to messageText,
            "timestamp" to System.currentTimeMillis()
        )

        // Log to verify chatId
        Log.d("ChatActivity", "Sending message in chat with chatId: $chatId")

        // Use the passed chatId to fetch the correct chat document
        val chatRef = db.collection("chats").document(chatId)

        chatRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Chat exists, add the message
                chatRef.collection("messages").add(newMessage)
                editTextMessage.setText("") // Clear the message field
            } else {
                Log.e("ChatActivity", "Chat document does not exist for chatId: $chatId")
            }
        }.addOnFailureListener { exception ->
            Log.e("ChatActivity", "Error fetching chat document", exception)
        }
    }





    private fun listenForMessages() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val currentUserId = currentUser.uid

        chatListener = db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) return@addSnapshotListener

                messageList.clear()
                snapshots?.documents?.mapNotNullTo(messageList) { doc ->
                    val senderId = doc.getString("senderId") ?: return@mapNotNullTo null
                    val text = doc.getString("text") ?: return@mapNotNullTo null
                    val timestamp = doc.getLong("timestamp")?.toString() ?: return@mapNotNullTo null

                    val messageType = if (senderId == currentUserId) Message.MessageType.SENT else Message.MessageType.RECEIVED

                    Message(senderId, text, timestamp, messageType)
                }

                messageAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messageList.size - 1)
            }
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
