package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

object DateConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }
}
