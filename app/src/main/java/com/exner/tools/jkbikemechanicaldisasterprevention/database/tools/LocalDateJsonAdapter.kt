package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlinx.datetime.LocalDate

class LocalDateJsonAdapter {

    @ToJson
    fun toJson(localDate: LocalDate): String {
        return localDate.toString()
    }

    @FromJson
    fun fromJson(json: String): LocalDate {
        return LocalDate.parse(json)
    }
}