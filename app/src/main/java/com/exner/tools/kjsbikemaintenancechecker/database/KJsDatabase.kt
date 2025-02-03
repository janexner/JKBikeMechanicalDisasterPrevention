package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.ActivityTags
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.entities.ComponentActivities
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Ride
import com.exner.tools.kjsbikemaintenancechecker.database.entities.RideUidByRideLevel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Tag
import com.exner.tools.kjsbikemaintenancechecker.database.entities.TemplateActivity
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivityWithBikeData
import com.exner.tools.kjsbikemaintenancechecker.database.views.ShelvedComponents

@Database(
    entities = [
        Activity::class,
        Tag::class,
        ActivityTags::class,
        Bike::class,
        Component::class,
        ComponentActivities::class,
        Ride::class,
        RideUidByRideLevel::class,
        TemplateActivity::class,
    ],
    views = [ActivityWithBikeData::class, ShelvedComponents::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DataConverter::class
)
abstract class KJsDatabase : RoomDatabase() {
    abstract fun dataDAO(): KJsDAO
}