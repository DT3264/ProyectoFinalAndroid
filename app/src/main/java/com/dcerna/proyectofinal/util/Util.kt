package com.dcerna.proyectofinal.util

import java.text.SimpleDateFormat
import java.util.*


fun getDate(milliSeconds: Long, dateFormat: String?): String? {
    val formatter = SimpleDateFormat(dateFormat)

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}