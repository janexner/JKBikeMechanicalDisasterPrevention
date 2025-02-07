package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

import kotlinx.datetime.Instant
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

