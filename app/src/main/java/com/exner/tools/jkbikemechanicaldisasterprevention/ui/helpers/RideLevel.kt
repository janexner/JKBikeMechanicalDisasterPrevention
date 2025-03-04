package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

import android.content.Context
import com.exner.tools.jkbikemechanicaldisasterprevention.R

class RideLevel {

    companion object {
        fun getRideLevelQuickRide(): Int {
            return 1
        }

        fun getRideLevelDayOut(): Int {
            return 2
        }

        fun getRideLevelHolidays(): Int {
            return 3
        }

        fun getListOfRideLevels(): List<Int> {
            return listOf(1, 2, 3)
        }

        fun getLabel(context: Context, level: Int?): String {
            when (level) {
                null -> return context.getString(R.string.ride_level_all_levels)

                1 -> return context.getString(R.string.ride_level_quick_ride)

                2 -> return context.getString(R.string.ride_level_day_out)

                3 -> return context.getString(R.string.ride_level_holidays)
            }

            return context.getString(R.string.ride_level_unknown)
        }

    }
}