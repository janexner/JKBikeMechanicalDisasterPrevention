package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class WearLevel {
    NEW,
    USED,
    DUE_FOR_REPLACEMENT
}

class WearLevelJsonAdapter {

    @ToJson
    fun toJson(wearLevel: WearLevel): String {
        return wearLevel.toString()
    }

    @FromJson
    fun fromJson(json: String): WearLevel {
        when (json) {
            "NEW" -> return WearLevel.NEW
            "USED" -> return WearLevel.USED
            "DUE_FOR_REPLACEMENT" -> return WearLevel.DUE_FOR_REPLACEMENT
        }
        return WearLevel.NEW
    }
}