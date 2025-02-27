package com.example.cs_topics_project_test.ui.ui.chat
import android.os.Parcel
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
data class Chat(
    val cid: String = "",
    val recipientName: String = "",
    val senderName: String = ""
) : Parcelable


