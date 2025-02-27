package com.exner.tools.jkbikemechanicaldisasterprevention.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.ActivityTags
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Ride
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.RideUidByRideLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Tag
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData

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
    version = 2,
    exportSchema = true
)
@TypeConverters(
    DataConverter::class
)
abstract class KJsDatabase : RoomDatabase() {
    abstract fun dataDAO(): KJsDAO
}