package com.example.cs_topics_project_test.chat

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import android.graphics.Rect
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.cs_topics_project_test.themes.ThemeManager
import com.google.firebase.firestore.FirebaseFirestore


class MessageAdapter(private val messageList: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == Message.MessageType.SENT.ordinal) {
            SentMessageViewHolder(inflater.inflate(R.layout.item_sent_message, parent, false))
        } else {
            ReceivedMessageViewHolder(inflater.inflate(R.layout.item_received_message, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message, position, messageList)
        }
    }



    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        return messageList[position].messageType.ordinal
    }

    // ViewHolder for SENT messages
    class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.messageText)
        private val messageBackground: View = view.findViewById(R.id.sentMessageBackground)

        fun bind(message: Message) {
            messageText.text = message.content
            val displayMetrics = itemView.context.resources.displayMetrics
            val maxWidth = (displayMetrics.widthPixels * 0.7).toInt() // 70% of screen width
            messageText.maxWidth = maxWidth
            val bg = ContextCompat.getDrawable(itemView.context, R.drawable.sent_message_background)?.mutate()
            bg!!.setTint(ThemeManager.currentThemeColors!!.chatAccent)
            messageBackground.background = bg
        }
    }

    // ViewHolder for RECEIVED messages
    class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.messageText)
        private val senderNameText: TextView = view.findViewById(R.id.senderNameTextView)
        private val profileImageView: ImageView = view.findViewById(R.id.profileImageView)

        fun bind(message: Message, position: Int, messageList: List<Message>) {
            messageText.text = message.content

            // Determine whether to show sender name & profile picture
            val showSenderInfo = position == 0 || messageList[position - 1].sender != message.sender

            if (showSenderInfo) {
                senderNameText.visibility = View.VISIBLE
                senderNameText.text = message.senderName
                profileImageView.visibility = View.VISIBLE

                // Load profile picture from Firebase
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .document(message.sender)
                    .collection("profilePicture")
                    .document("pfpImg")
                    .get()
                    .addOnSuccessListener { doc ->
                        val base64Image = doc.getString("pfp")
                        if (!base64Image.isNullOrEmpty()) {
                            try {
                                val decodedBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
                                val bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                Glide.with(profileImageView.context)
                                    .load(bitmap)
                                    .circleCrop()
                                    .placeholder(R.drawable.ic_pfp_circular)
                                    .into(profileImageView)
                            } catch (e: Exception) {
                                profileImageView.setImageResource(R.drawable.ic_pfp_circular)
                            }
                        } else {
                            profileImageView.setImageResource(R.drawable.ic_pfp_circular)
                        }
                    }
                    .addOnFailureListener {
                        profileImageView.setImageResource(R.drawable.ic_pfp_circular)
                    }
            } else {
                senderNameText.visibility = View.GONE
                profileImageView.visibility = View.INVISIBLE // Hide but keep space so layout doesn't jump
            }
        }

    }



    class MessageSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (position == RecyclerView.NO_POSITION) return

            // Apply top margin for all items
            outRect.top = 1

            // Optional: Add bottom spacing for the last item
            if (position == state.itemCount - 1) {
                outRect.bottom = 1
            }
        }
    }
}

