package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
data class Ride(
    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "created_instant") val createdInstant: Instant,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
