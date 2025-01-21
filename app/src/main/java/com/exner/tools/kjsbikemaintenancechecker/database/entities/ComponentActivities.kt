package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ComponentActivities(
    @ColumnInfo(name = "component_uid") val componentUid: Long,
    @ColumnInfo(name = "activity_uid") val activityUid: Long,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
