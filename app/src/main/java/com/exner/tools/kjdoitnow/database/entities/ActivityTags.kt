package com.exner.tools.kjdoitnow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityTags(
    @ColumnInfo(name = "activity_uid") val activityUid: Long,
    @ColumnInfo(name = "tag_uid") val tagUid: Long,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
