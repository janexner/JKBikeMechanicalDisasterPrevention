package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.TypeConverter
import java.time.LocalDateTime

object DateConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}