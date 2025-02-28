package com.exner.tools.jkbikemechanicaldisasterprevention.database

import androidx.room.TypeConverter
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel
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
    fun fromWearLevelString(value: String?): WearLevel {
        when (value) {
            "NEW" -> return WearLevel.NEW
            "USED" -> return WearLevel.USED
            "DUE_FOR_REPLACEMENT" -> return WearLevel.DUE_FOR_REPLACEMENT
        }
        // default is NEW
        return WearLevel.NEW
    }

    @TypeConverter
    fun wearLevelToString(wearLevel: WearLevel?): String? {
        return wearLevel?.toString() // good for the database, do not localise!
    }
}
