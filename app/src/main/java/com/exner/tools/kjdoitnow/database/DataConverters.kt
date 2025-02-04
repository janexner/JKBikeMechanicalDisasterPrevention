package com.exner.tools.kjdoitnow.database

import androidx.room.TypeConverter
import com.exner.tools.kjdoitnow.ui.helpers.RideLevel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

object DataConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromEpochMillis(value: Long?): Instant? {
        return value.let {
            if (it != null) {
                Instant.fromEpochMilliseconds(it)
            } else {
                null
            }
        }
    }

    @TypeConverter
    fun instantToEpochMillis(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }

    @TypeConverter
    fun rideLevelToString(rideLevel: RideLevel?): String? {
        return if (rideLevel != null) {
            "${rideLevel.level}|${rideLevel.name}"
        } else {
            null
        }
    }

    @TypeConverter
    fun fromString(rideLevelString: String?): RideLevel? {
        if (rideLevelString == null) {
            return null
        }
        val values = rideLevelString.split('|')
        return RideLevel(values[0].toInt(), values[1])
    }
}
