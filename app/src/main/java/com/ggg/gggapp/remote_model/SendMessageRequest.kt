package com.ggg.gggapp.remote_model

data class SendMessageRequest(
    val chat_id: Long,
    val message: String,
    val user_id: Long
)