package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Bike(
    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "build_date") val buildDate: LocalDateTime,
    @ColumnInfo(name = "mileage") val mileage: Int = 0,
    @ColumnInfo(name = "last_used_date") val lastUsedDate: LocalDateTime,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
