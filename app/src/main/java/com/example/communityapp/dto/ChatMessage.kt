package com.example.communityapp.dto

data class ChatMessage(
    val userId: String = "",
    val message: String = "",
    val createdAt: Long = 0
)
