package com.ggg.gggapp.model

data class Role(
    val id: Long,
    val permissions: Permissions,
    val role_name: String
)

data class Permissions(
    val id: Long,
    val name: String
)