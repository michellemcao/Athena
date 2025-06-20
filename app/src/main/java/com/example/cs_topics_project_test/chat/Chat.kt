package com.example.cs_topics_project_test.chat
import android.os.Parcel
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
data class Chat(
    val cid: String = "",
    val recipientName: String = "",
    val recipientUsername: String?, // only for individual chats, for now
    val senderName: String = "",
    val isGroup: Boolean = false
) : Parcelable


