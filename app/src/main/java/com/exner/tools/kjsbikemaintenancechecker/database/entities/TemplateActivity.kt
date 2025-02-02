package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@Entity
data class TemplateActivity(
    @ColumnInfo(name = "ride_level") val rideLevel: Int,

    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,

    @ColumnInfo(name = "bike_uid") val bikeUid: Long,
    @ColumnInfo(name = "is_ebike_specific") val isEBikeSpecific: Boolean = false,

    @ColumnInfo(name = "created_date") val createdDate: LocalDate,
    @ColumnInfo(name = "due_date") val dueDate: LocalDate?,
    @ColumnInfo(name = "done_date") val doneDate: Instant?,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
