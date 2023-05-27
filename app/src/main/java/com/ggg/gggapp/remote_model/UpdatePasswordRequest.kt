package com.ggg.gggapp.remote_model

data class UpdatePasswordRequest(
    val id: Long,
    val username: String,
    val password: String
)