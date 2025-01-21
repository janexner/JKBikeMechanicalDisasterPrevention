package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BikeActivities(
    @ColumnInfo(name = "bike_uid") val bikeUid: Long,
    @ColumnInfo(name = "activity_uid") val activityUid: Long,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
