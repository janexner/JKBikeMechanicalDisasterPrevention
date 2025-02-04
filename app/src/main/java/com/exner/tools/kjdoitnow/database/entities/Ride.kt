package com.exner.tools.kjdoitnow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.exner.tools.kjdoitnow.ui.helpers.RideLevel
import kotlinx.datetime.Instant

@Entity
data class Ride(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "level") val level: RideLevel,

    @ColumnInfo(name = "created_instant") val createdInstant: Instant,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
