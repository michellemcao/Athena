package com.example.cs_topics_project_test.ui.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R

class ChatListAdapter(
    private val chatList: MutableList<Chat>,  // Changed to Chat
    private val onItemClick: (Chat) -> Unit    // Changed to Chat
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]  // Changed to Chat
        holder.bind(chat)  // Changed to Chat
    }

    override fun getItemCount(): Int = chatList.size

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.chatName)

        fun bind(chat: Chat) {  // Changed to Chat
            // Assuming Chat has senderName and recipientName
            nameTextView.text = "${chat.senderName} - ${chat.recipientName}"  // You can customize this text as needed
            itemView.setOnClickListener {
                onItemClick(chat)  // Changed to Chat
            }
        }
    }
}
