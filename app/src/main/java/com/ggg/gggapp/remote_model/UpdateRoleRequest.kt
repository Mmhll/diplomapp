package com.ggg.gggapp.remote_model

data class UpdateRoleRequest(
    val id: Long,
    val rolename: String,
    val permission: String
)