package com.exner.tools.kjdoitnow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
data class RideUidByRideLevel(
    @ColumnInfo(name = "ride_level") val rideLevel: Int,
    @ColumnInfo(name = "ride_uid") val rideUid: Long,
    @ColumnInfo(name = "created_instant") val createdInstant: Instant,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
