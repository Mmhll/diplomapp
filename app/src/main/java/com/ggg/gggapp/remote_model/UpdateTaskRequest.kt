package com.ggg.gggapp.remote_model

data class UpdateTaskRequest(
    val deadline: String,
    val description: String,
    val name: String,
    val task_id: Long
)