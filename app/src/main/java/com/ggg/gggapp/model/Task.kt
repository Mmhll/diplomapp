package com.ggg.gggapp.model

data class Task(
    val creation_date: String,
    val creator: User,
    val date_of_update: String,
    val deadline: String,
    val description: String,
    val status: String,
    val executor: User,
    val id: Long,
    val members: ArrayList<User>,
    val name: String
)