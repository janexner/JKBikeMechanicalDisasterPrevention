package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Activity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,

    @ColumnInfo(name = "created") val created: LocalDateTime,
    @ColumnInfo(name = "due_date") val dueDate: LocalDateTime,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
