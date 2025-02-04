package com.exner.tools.kjdoitnow.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exner.tools.kjdoitnow.database.entities.Activity
import com.exner.tools.kjdoitnow.database.entities.ActivityTags
import com.exner.tools.kjdoitnow.database.entities.Bike
import com.exner.tools.kjdoitnow.database.entities.Ride
import com.exner.tools.kjdoitnow.database.entities.RideUidByRideLevel
import com.exner.tools.kjdoitnow.database.entities.Tag
import com.exner.tools.kjdoitnow.database.entities.TemplateActivity
import com.exner.tools.kjdoitnow.database.views.ActivityWithBikeData

@Database(
    entities = [
        Activity::class,
        Tag::class,
        ActivityTags::class,
        Bike::class,
        Ride::class,
        RideUidByRideLevel::class,
        TemplateActivity::class,
    ],
    views = [ActivityWithBikeData::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DataConverter::class
)
abstract class KJsDatabase : RoomDatabase() {
    abstract fun dataDAO(): KJsDAO
}