package com.ggg.gggapp.utils

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

fun generateInitials(secondName: String, firstName: String, middleName: String): String{

    return "${lastnameUppercase(secondName)} ${firstToUppercase(firstName)} ${firstToUppercase(middleName)}"
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