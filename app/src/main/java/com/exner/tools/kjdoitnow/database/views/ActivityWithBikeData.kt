package com.exner.tools.kjdoitnow.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.exner.tools.kjdoitnow.ui.helpers.RideLevel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@DatabaseView(
    "SELECT " +
            "b.name as bike_name, b.uid as bike_uid, " +
            "a.title as activity_title, a.description as activity_description, a.is_completed as activity_is_completed, a.ride_uid as activity_ride_uid, a.created_instant as activity_created_instant, a.due_date as activity_due_date, a.done_instant as activity_done_instant, a.is_ebike_specific as activity_is_ebike_specific, a.ride_level as activity_ride_level, " +
            "a.uid as activity_uid " +
            "FROM Activity a " +
            "LEFT JOIN Bike b ON b.uid = a.bike_uid " +
            "ORDER BY a.due_date DESC;"
)
data class ActivityWithBikeData(
    @ColumnInfo(name = "bike_name") val bikeName: String?,
    @ColumnInfo(name = "bike_uid") val bikeUid: Long?,
    @ColumnInfo(name = "activity_title") val activityTitle: String,
    @ColumnInfo(name = "activity_description") val activityDescription: String,
    @ColumnInfo(name = "activity_is_completed") val activityIsCompleted: Boolean,
    @ColumnInfo(name = "activity_ride_uid") val rideUid: Long?,
    @ColumnInfo(name = "activity_created_instant") val activityCreatedInstant: Instant,
    @ColumnInfo(name = "activity_due_date") val activityDueDate: LocalDate?,
    @ColumnInfo(name = "activity_done_instant") val activityDoneDateInstant: Instant?,
    @ColumnInfo(name = "activity_is_ebike_specific") val isEBikeSpecific: Boolean,
    @ColumnInfo(name = "activity_ride_level") val activityRideLevel: RideLevel,
    @ColumnInfo(name = "activity_uid") val activityUid: Long
)
