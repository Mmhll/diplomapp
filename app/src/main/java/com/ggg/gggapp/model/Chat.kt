package com.ggg.gggapp.model

data class Chat(
    val created_at: String,
    val id: Long,
    val messages: List<Message>,
    val title: String,
    val updated_at: String,
    val users: List<User>
)