package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.ActivityTags
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Tag

@Database(
    entities = [Activity::class, Tag::class, ActivityTags::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateConverter::class
)
abstract class KJsDatabase : RoomDatabase() {
    abstract fun dataDAO(): KJsDAO
}