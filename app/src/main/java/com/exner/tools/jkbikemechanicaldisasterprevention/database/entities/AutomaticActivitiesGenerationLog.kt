package com.exner.tools.jkbikemechanicaldisasterprevention.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
data class AutomaticActivitiesGenerationLog(
    @ColumnInfo(name = "created_instant") val createdInstant: Instant,
    @ColumnInfo(name = "created_component_uid") val createdComponentUid: Long,
    @ColumnInfo(name = "activity_uid") val activityUid: Long,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
