package com.example.cs_topics_project_test.ui.ui.chat
data class Message(
    val sender: String,
    val senderName: String,
    val content: String,
    val timestamp: String,
    var messageType: MessageType
) {
    enum class MessageType {
        SENT,
        RECEIVED
    }

    constructor() : this("", "", "", "", MessageType.SENT)
}