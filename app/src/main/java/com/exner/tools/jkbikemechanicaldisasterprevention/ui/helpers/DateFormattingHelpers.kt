package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun convertMillisToDate(millis: Long): String {
    return Instant.fromEpochMilliseconds(millis)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
}

fun convertMillisToDateAndTime(millis: Long): String {
    return Instant.fromEpochMilliseconds(millis)
        .toLocalDateTime(TimeZone.currentSystemDefault()).toString()
}

fun Long?.toLocalDate(): LocalDate? {
    if (this != null) {
        val lastUsedDateInstant = Instant.fromEpochMilliseconds(this)
        val lastUsedDate =
            lastUsedDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return lastUsedDate
    }
    return null
}
