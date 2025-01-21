package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Activity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,

    @ColumnInfo(name = "created_date") val createdDate: LocalDateTime,
    @ColumnInfo(name = "due_date") val dueDate: LocalDateTime,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
