package com.ggg.gggapp.utils

import com.ggg.gggapp.model.UserData

fun generateEnding(count: Int): String {
    val countString = count.toString()
    return when (countString.last()) {
        '1' -> {
            ""
        }
        '2', '3', '4' -> {
            "а"
        }
        else -> {
            "ов"
        }
    }
}

fun generateToken(token: String): String{
    return "Bearer $token"
}

fun generateInitials(user: UserData): String{
    return "${lastnameUppercase(user.lastname)} ${firstToUppercase(user.firstname)} ${firstToUppercase(user.middlename)}"
}

private fun firstToUppercase(string: String): String{
    return if (string.isNotEmpty()){
        string[0].uppercase()
    }
    else {
        ""
    }
}

private fun lastnameUppercase(lastname: String): String{
    return if(lastname.isNotEmpty()){
        lastname.replaceFirstChar { char -> char.uppercase()}
    } else {
        ""
    }
}