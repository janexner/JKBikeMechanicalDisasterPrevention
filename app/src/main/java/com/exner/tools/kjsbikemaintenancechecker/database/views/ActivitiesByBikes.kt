package com.exner.tools.kjsbikemaintenancechecker.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import kotlinx.datetime.LocalDate

@DatabaseView(
    "SELECT b.name as bike_name, " +
            "a.title as activity_title, " +
            "a.description as activity_description, " +
            "a.is_completed as activity_is_completed, " +
            "a.created_date as activity_created_date, " +
            "a.due_date as activity_due_date, " +
            "a.uid as activity_uid " +
            "FROM " +
            "Activity a " +
            "LEFT JOIN BikeActivities ba ON a.uid = ba.activity_uid " +
            "LEFT JOIN Bike b ON b.uid = ba.bike_uid " +
            "ORDER BY a.due_date DESC;"
)
data class ActivitiesByBikes(
    @ColumnInfo(name = "bike_name") val bikeName: String?,
    @ColumnInfo(name = "activity_title") val activityTitle: String,
    @ColumnInfo(name = "activity_description") val activityDescription: String,
    @ColumnInfo(name = "activity_is_completed") val activityIsCompleted: Boolean,
    @ColumnInfo(name = "activity_created_date") val activityCreatedDate: LocalDate,
    @ColumnInfo(name = "activity_due_date") val activityDueDate: LocalDate?,
    @ColumnInfo(name = "activity_uid") val activityUid: Long
)