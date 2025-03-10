package com.exner.tools.jkbikemechanicaldisasterprevention.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.datetime.LocalDate

@Entity
@JsonClass(generateAdapter = true)
data class Bike(
    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "build_date") val buildDate: LocalDate,
    @ColumnInfo(name = "mileage") val mileage: Int = 0,
    @ColumnInfo(name = "last_used_date") val lastUsedDate: LocalDate?,

    @ColumnInfo(name = "is_electric") val isElectric: Boolean = false,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
