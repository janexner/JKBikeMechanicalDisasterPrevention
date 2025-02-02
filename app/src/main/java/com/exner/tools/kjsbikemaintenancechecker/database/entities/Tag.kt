package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @ColumnInfo(name = "title") val title: String,
//    @ColumnInfo(name = "colour") val colour: Color,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
