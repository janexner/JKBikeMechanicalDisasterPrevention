package com.exner.tools.kjdoitnow.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.exner.tools.kjdoitnow.ui.helpers.RideLevel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@Entity
data class Activity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false,

    @ColumnInfo(name = "bike_uid") val bikeUid: Long?,
    @ColumnInfo(name = "is_ebike_specific") val isEBikeSpecific: Boolean = false,
    @ColumnInfo(name = "ride_level") val rideLevel: RideLevel?,

    @ColumnInfo(name = "ride_uid") val rideUid: Long?,
    @ColumnInfo(name = "created_instant") val createdInstant: Instant,
    @ColumnInfo(name = "due_date") val dueDate: LocalDate?,
    @ColumnInfo(name = "done_instant") val doneInstant: Instant?,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
