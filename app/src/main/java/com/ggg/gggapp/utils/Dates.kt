package com.ggg.gggapp.utils

import android.icu.util.Calendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrentDate(): Array<Int>{
    val calendar = Calendar.getInstance()
    return arrayOf(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}
fun getCurrentDateTime(): Array<Int>{
    val calendar = Calendar.getInstance()
    return arrayOf(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        calendar.get(Calendar.SECOND),
        calendar.get(Calendar.MILLISECOND)
    )
}

fun makeDateTimeValid(dateTimeString: String): String {
    val formatDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val dateTime = LocalDateTime.parse(dateTimeString.substringBefore('+'))
    return formatDateTime.format(dateTime)
}