package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

data class RideLevel(
    val level: Int,
    val name: String,
) {

    companion object {
        fun getRideLevelQuickRide(): RideLevel {
            return RideLevel(
                level = 1,
                name = "Quick ride"
            )
        }

        fun getRideLevelDayOut(): RideLevel {
            return RideLevel(
                level = 2,
                name = "Day out"
            )
        }

        fun getRideLevelHolidays(): RideLevel {
            return RideLevel(
                level = 3,
                name = "Holidays"
            )
        }

        fun getListOfRideLevels(): List<RideLevel> {
            return listOf(
                getRideLevelQuickRide(),
                getRideLevelDayOut(),
                getRideLevelHolidays()
            )
        }
    }
}

