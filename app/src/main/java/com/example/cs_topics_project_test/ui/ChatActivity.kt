package com.example.cs_topics_project_test.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

class ChatActivity : AppCompatActivity() {




    companion object {
        private const val EXTRA_PERSON = "extra_person"

        // Factory method to create an intent for ChatActivity with a Person object
        fun createIntent(context: Context, person: Person): Intent {
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_PERSON, person)
            }
        }
    }

    // Declare views and variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private var messageList: MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val recyclerViewMessages: RecyclerView = findViewById(R.id.recyclerViewMessages)
        val spacing = resources.getDimensionPixelSize(R.dimen.message_spacing)

        recyclerViewMessages.addItemDecoration(MessageAdapter.MessageSpacingDecoration(spacing))

        // Retrieve the Person object passed via intent
        val person: Person? = intent.getParcelableExtra(EXTRA_PERSON)

        person?.let {
            // Set the title to the person's name
            setTitle(it.name)

            // Set up the toolbar and enable the back button
            val toolbar: Toolbar = findViewById(R.id.toolbarChat)
            setSupportActionBar(toolbar)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)

            // Setup RecyclerView for messages
            recyclerView = findViewById(R.id.recyclerViewMessages)
            recyclerView.layoutManager = LinearLayoutManager(this) // No reverse layout, messages appear from top to bottom
            // recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)  // Reverse layout
            messageAdapter = MessageAdapter(messageList)
            recyclerView.adapter = messageAdapter

            // Scroll to the bottom when the screen is first loaded

            recyclerView.scrollToPosition(messageList.size - 1)

            // Setup message input views
            editTextMessage = findViewById(R.id.editTextMessage)
            sendButton = findViewById(R.id.sendButton)

            // Setup send button click listener
            sendButton.setOnClickListener {
                val messageText = editTextMessage.text.toString()
                if (messageText.isNotEmpty()) {
                    val newMessage = Message(
                        sender = "You", // Update with dynamic sender name
                        content = messageText,
                        timestamp = System.currentTimeMillis().toString(),
                        messageType = Message.MessageType.SENT
                    )
                    messageList.add(newMessage)
                    messageAdapter.notifyItemInserted(messageList.size - 1)
                    recyclerView.scrollToPosition(messageList.size - 1) // Scroll to the latest message
                    editTextMessage.text.clear()
                }
            }

        } ?: throw IllegalArgumentException("Person must be provided")

    }

    // Handle the back button press in the toolbar
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

