package com.example.cs_topics_project_test.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide
import android.util.Base64
import android.graphics.BitmapFactory


class ChatListAdapter(
    private val chatList: MutableList<Chat>,
    private val onItemClick: (Chat) -> Unit,
    private val onDeleteClick: (Chat) -> Unit,
    private val onBlockClick: (Chat) -> Unit,
    private val onOptionsClick: (Chat) -> Unit // k
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
        private val profilePicture: ImageView = itemView.findViewById(R.id.profilePicture)

        fun bind(chat: Chat) {
            chatName.text = chat.recipientName

            val isGroupChat = chat.isGroup

            if (isGroupChat) {
                profilePicture.setImageResource(R.drawable.baseline_groups_24)
            } else {
                val db = FirebaseFirestore.getInstance()
                val username = chat.recipientUsername
                Log.d("ChatAdapter", "Searching user with username: $username")

                if (username != null) {
                    db.collection("users")
                        .whereEqualTo("username", username)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { result ->
                            if (!result.isEmpty) {
                                val userDoc = result.documents[0]
                                val uid = userDoc.id

                                db.collection("users")
                                    .document(uid)
                                    .collection("profilePicture")
                                    .document("pfpImg")
                                    .get()
                                    .addOnSuccessListener { pfpDoc ->
                                        val pfpBase64 = pfpDoc.getString("pfp")
                                        if (!pfpBase64.isNullOrEmpty()) {
                                            try {
                                                val decodedBytes = Base64.decode(pfpBase64, Base64.DEFAULT)
                                                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                                Glide.with(profilePicture.context)
                                                    .load(bitmap)
                                                    .circleCrop()
                                                    .placeholder(R.drawable.ic_pfp_circular)
                                                    .into(profilePicture)

                                            } catch (e: IllegalArgumentException) {
                                                profilePicture.setImageResource(R.drawable.ic_pfp_circular)
                                            }
                                        } else {
                                            profilePicture.setImageResource(R.drawable.ic_pfp_circular)
                                        }
                                    }
                                    .addOnFailureListener {
                                        profilePicture.setImageResource(R.drawable.ic_pfp_circular)
                                    }
                            } else {
                                profilePicture.setImageResource(R.drawable.ic_pfp_circular)
                            }
                        }
                        .addOnFailureListener {
                            profilePicture.setImageResource(R.drawable.ic_pfp_circular)
                        }
                } else {
                    profilePicture.setImageResource(R.drawable.ic_pfp_circular)
                }
            }

            itemView.setOnClickListener { onItemClick(chat) }
            optionsButton.setOnClickListener { onOptionsClick(chat) }
        }



    }
}

