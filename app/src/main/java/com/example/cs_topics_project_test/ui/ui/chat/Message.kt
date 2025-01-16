package com.example.cs_topics_project_test.ui.ui.chat
data class Message(
    val sender: String,
    val content: String,
    val timestamp: String,
    val messageType: MessageType
) {
    enum class MessageType {
        SENT,
        RECEIVED
    }
}