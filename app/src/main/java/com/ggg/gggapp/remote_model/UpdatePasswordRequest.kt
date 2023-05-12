package com.ggg.gggapp.remote_model

data class UpdatePasswordRequest(
    val id: Long,
    val password: String,
    val username: String
)