package com.exner.tools.jkbikemechanicaldisasterprevention.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.datetime.LocalDate

@Entity
@JsonClass(generateAdapter = true)
data class Component(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "acquisition_date") val acquisitionDate: LocalDate?,
    @ColumnInfo(name = "first_use_date") val firstUseDate: LocalDate?,
    @ColumnInfo(name = "last_check_date") val lastCheckDate: LocalDate?,

    @ColumnInfo(name = "bike_uid") val bikeUid: Long?,

    @ColumnInfo(name = "check_interval_miles") val checkIntervalMiles: Int?,
    @ColumnInfo(name = "check_interval_days") val checkIntervalDays: Int?,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
