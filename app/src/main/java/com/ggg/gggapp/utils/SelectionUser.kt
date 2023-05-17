package com.ggg.gggapp.utils

import com.ggg.gggapp.model.User

data class SelectionUser(
    val users: List<User>,
    var isSelected: Boolean = false
)
