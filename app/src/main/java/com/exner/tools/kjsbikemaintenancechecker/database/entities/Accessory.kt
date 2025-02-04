package com.exner.tools.kjsbikemaintenancechecker.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
data class Accessory(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,

    @ColumnInfo(name = "bike_uid") val bikeUid: Long?,
    @ColumnInfo(name = "parent_accessory_uid") val parentAccessoryUid: Long?,

    @ColumnInfo(name = "acquisition_date") val acquisitionDate: LocalDate,
    @ColumnInfo(name = "last_used_date") val lastUsedDate: LocalDate?,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
