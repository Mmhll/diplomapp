package com.ggg.gggapp.utils

fun getEnding(count: Int): String {
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