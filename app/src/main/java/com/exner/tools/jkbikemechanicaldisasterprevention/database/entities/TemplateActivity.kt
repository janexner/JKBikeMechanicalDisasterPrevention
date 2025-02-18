package com.exner.tools.jkbikemechanicaldisasterprevention.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class TemplateActivity(
    @ColumnInfo(name = "ride_level") val rideLevel: Int?,

    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,

    @ColumnInfo(name = "is_ebike_specific") val isEBikeSpecific: Boolean = false,

    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)
