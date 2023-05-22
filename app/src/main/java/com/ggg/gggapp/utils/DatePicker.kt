package com.ggg.gggapp.utils

import android.icu.util.Calendar

fun getCurrentDate(): Array<Int>{
    val calendar = Calendar.getInstance()
    return arrayOf(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}