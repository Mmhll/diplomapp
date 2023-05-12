package com.ggg.gggapp.remote_model

data class UpdateUserRequest(
    val id: Long,
    val email: String,
    val firstname: String,
    val lastname: String,
    val middlename: String,
    val username: String
)