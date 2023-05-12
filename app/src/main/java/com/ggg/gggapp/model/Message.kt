package com.ggg.gggapp.model

data class Message (
    val id: Long,
    val text: String,
    val created_at: String,
    val is_updated: Boolean,
    val user: User
)