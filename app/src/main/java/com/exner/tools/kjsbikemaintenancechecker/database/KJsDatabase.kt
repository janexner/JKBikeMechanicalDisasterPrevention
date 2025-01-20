package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Activity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateConverter::class
)
abstract class KJsDatabase : RoomDatabase() {
    abstract fun dataDAO(): KJsDAO
}