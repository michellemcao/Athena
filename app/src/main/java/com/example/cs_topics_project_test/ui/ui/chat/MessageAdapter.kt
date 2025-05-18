package com.example.cs_topics_project_test.ui.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import android.graphics.Rect


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

        fun bind(message: Message) {
            messageText.text = message.content
            val displayMetrics = itemView.context.resources.displayMetrics
            val maxWidth = (displayMetrics.widthPixels * 0.7).toInt() // 70% of screen width
            messageText.maxWidth = maxWidth

        }
    }

    // ViewHolder for RECEIVED messages
    class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText: TextView = view.findViewById(R.id.messageText)
        private val senderNameText: TextView = view.findViewById(R.id.senderNameTextView)

        fun bind(message: Message, position: Int, messageList: List<Message>) {
            messageText.text = message.content

            // Only show name if this is the first message OR the previous message was from a different sender
            val showSenderName = when {
                position == 0 -> true
                else -> {
                    val prevMessage = messageList[position - 1]
                    prevMessage.sender != message.sender
                }
            }

            if (showSenderName) {
                senderNameText.visibility = View.VISIBLE
                senderNameText.text = message.senderName
            } else {
                senderNameText.visibility = View.GONE
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

