package com.ggg.gggapp.model

data class Chat(
    val id: Long,
    val title: String,
    val createdAt: String,
    val updatedAt: String,
    val messages: List<Message>,
    val users: List<User>
)