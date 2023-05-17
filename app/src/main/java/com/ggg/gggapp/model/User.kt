package com.ggg.gggapp.model

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val roles: ArrayList<Role>,
    val userData: UserData
)

data class UserData(
    val id: Long,
    var firstname: String,
    var lastname: String,
    var middlename: String,
    val phone_number: String
)