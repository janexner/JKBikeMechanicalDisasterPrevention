package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
data class Component(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,

    @ColumnInfo(name = "bike_uid") val bikeUid: Long,
    @ColumnInfo(name = "parent_component_uid") val parentComponentUid: Long?,

    @ColumnInfo(name = "acquisition_date") val acquisitionDate: LocalDate,
    @ColumnInfo(name = "mileage") val mileage: Int,
    @ColumnInfo(name = "last_used_date") val lastUsedDate: LocalDate?,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
