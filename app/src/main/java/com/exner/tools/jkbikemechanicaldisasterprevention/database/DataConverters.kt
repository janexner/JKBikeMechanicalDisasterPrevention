package com.exner.tools.jkbikemechanicaldisasterprevention.database

import androidx.room.TypeConverter
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
}
