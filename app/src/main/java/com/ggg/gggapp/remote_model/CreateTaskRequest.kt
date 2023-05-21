package com.ggg.gggapp.remote_model

data class CreateTaskRequest(
    val creation_date: String,
    val creator_id: Long,
    var deadline: String? = null,
    val description: String,
    val executor_id: Long,
    val members: List<Long>,
    val task_name: String
)