package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import android.content.Context
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

enum class RetirementReason {
    WORN,
    CRASH,
    UPGRADE,
    NO_LONGER_NEEDED,
}

fun toLocalisedString(retirementReason: RetirementReason, context: Context): String {
    return when(retirementReason) {
        RetirementReason.WORN -> {
            context.getString(R.string.dropdown_item_retirement_reason_worn)
        }
        RetirementReason.CRASH -> {
            context.getString(R.string.dropdown_item_retirement_reason_crash)
        }
        RetirementReason.UPGRADE -> {
            context.getString(R.string.dropdown_item_retirement_reason_upgrade)
        }
        RetirementReason.NO_LONGER_NEEDED -> {
            context.getString(R.string.dropdown_item_retirement_reason_no_longer_needed)
        }
    }
}

class RetirementReasonJsonAdapter {

    @ToJson
    fun toJson(retirementReason: RetirementReason): String {
        return retirementReason.toString()
    }

    @FromJson
    fun fromJson(json: String): RetirementReason {
        when(json) {
            "WORN" -> return RetirementReason.WORN
            "CRASH" -> return RetirementReason.CRASH
            "UPGRADE" -> return RetirementReason.UPGRADE
            "NO_LONGER_NEEDED" -> return RetirementReason.NO_LONGER_NEEDED
        }
        return RetirementReason.NO_LONGER_NEEDED
    }
}
