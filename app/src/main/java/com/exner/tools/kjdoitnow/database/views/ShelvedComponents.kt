package com.exner.tools.kjdoitnow.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import kotlinx.datetime.LocalDate

@DatabaseView(
    "SELECT * FROM Component WHERE bike_uid=NULL OR bike_uid<1;"
)
data class ShelvedComponents(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,

    @ColumnInfo(name = "bike_uid") val bikeUid: Long,
    @ColumnInfo(name = "parent_component_uid") val parentComponentUid: Long?,

    @ColumnInfo(name = "acquisition_date") val acquisitionDate: LocalDate,
    @ColumnInfo(name = "mileage") val mileage: Int,
    @ColumnInfo(name = "last_used_date") val lastUsedDate: LocalDate?
)
