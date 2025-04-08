package com.example.cs_topics_project_test.ui.ui.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R

class ChatListAdapter(
    private val chatList: MutableList<Chat>,
    private val onItemClick: (Chat) -> Unit,
    private val onDeleteClick: (Chat) -> Unit,
    private val onBlockClick: (Chat) -> Unit,
    private val onOptionsClick: (Chat) -> Unit // 👈 NEW callback
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        Log.d("ChatListAdapter", "Binding chat: ${chat.recipientName}")
        holder.chatName.text = chat.recipientName // Set only the recipient's name
        holder.bind(chat)
    }

    override fun getItemCount(): Int = chatList.size

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatName: TextView = itemView.findViewById(R.id.chatName) // Assuming the TextView ID is `chatTitle`
        private val optionsButton: ImageButton = itemView.findViewById(R.id.chatOptionsButton)

        fun bind(chat: Chat) {
            // Set the full name in case you want to use it in other places
            itemView.setOnClickListener { onItemClick(chat) }

            optionsButton.setOnClickListener { onOptionsClick(chat) }
        }
    }
}

