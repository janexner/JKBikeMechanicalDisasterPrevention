package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import android.content.Context
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class WearLevel {
    NEW,
    USED,
    DUE_FOR_REPLACEMENT,
}

fun toLocalisedString(wearLevel: WearLevel, context: Context): String {
    return when (wearLevel) {
        WearLevel.NEW -> {
            context.getString(R.string.dropdown_item_wear_level_new)
        }

        WearLevel.USED -> {
            context.getString(R.string.dropdown_item_wear_level_used)
        }

        WearLevel.DUE_FOR_REPLACEMENT -> {
            context.getString(R.string.dropdown_item_wear_level_due_for_replacement)
        }
    }
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