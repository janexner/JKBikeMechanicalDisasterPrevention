package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlinx.datetime.Instant

class InstantJsonAdapter {

    @ToJson
    fun toJson(instant: Instant): String {
        return instant.toString()
    }

    @FromJson
    fun fromJson(json: String): Instant {
        return Instant.parse(json)
    }
}