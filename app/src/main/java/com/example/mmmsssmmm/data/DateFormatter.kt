package com.example.mmmsssmmm.data

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.util.Locale

fun formatHumanReadableDate(isoString: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoString)

        val localTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault())

        localTime.format(formatter)
    } catch (e: Exception) {
        isoString
    }
}