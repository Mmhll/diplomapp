package com.ggg.gggapp.remote_model

data class RegisterUserRequest(
    val email: String,
    val firstname: String,
    val lastname: String,
    val middlename: String,
    val password: String,
    val phone_number: String
)