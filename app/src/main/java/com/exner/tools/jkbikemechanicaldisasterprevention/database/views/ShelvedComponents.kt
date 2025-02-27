package com.exner.tools.jkbikemechanicaldisasterprevention.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel
import kotlinx.datetime.LocalDate

@DatabaseView(
    "SELECT name, description, acquisition_date, first_use_date, last_check_date, last_check_mileage, current_mileage, bike_uid, title_for_automatic_activities, wear_level, retirement_date, check_interval_miles, check_interval_days, " +
            " uid " +
            "FROM Component WHERE bike_uid = NULL AND retirement_date = NULL ORDER BY acquisition_date DESC;"
)
data class ShelvedComponents(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "acquisition_date") val acquisitionDate: LocalDate?,
    @ColumnInfo(name = "first_use_date") val firstUseDate: LocalDate?,
    @ColumnInfo(name = "last_check_date") val lastCheckDate: LocalDate?,
    @ColumnInfo(name = "last_check_mileage") val lastCheckMileage: Int?,
    @ColumnInfo(name = "current_mileage") val currentMileage: Int?,

    @ColumnInfo(name = "bike_uid") val bikeUid: Long?,

    @ColumnInfo(name = "title_for_automatic_activities") val titleForAutomaticActivities: String?,

    @ColumnInfo(name = "wear_level") val wearLevel: WearLevel?,
    @ColumnInfo(name = "retirement_date") val retirementDate: LocalDate?,

    @ColumnInfo(name = "check_interval_miles") val checkIntervalMiles: Int?,
    @ColumnInfo(name = "check_interval_days") val checkIntervalDays: Int?,

    @ColumnInfo(name = "uid") val uid: Long
)
