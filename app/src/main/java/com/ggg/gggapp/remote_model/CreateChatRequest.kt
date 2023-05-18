package com.ggg.gggapp.remote_model

data class CreateChatRequest(
    val members: ArrayList<Long>,
    val name: String
)