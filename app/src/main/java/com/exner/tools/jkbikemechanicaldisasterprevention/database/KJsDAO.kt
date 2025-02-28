package com.exner.tools.jkbikemechanicaldisasterprevention.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.AutomaticActivitiesGenerationLog
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Ride
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.RideUidByRideLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.RetiredComponents
import kotlinx.coroutines.flow.Flow

@Dao
interface KJsDAO {
    //
    // OBSERVERS - return tables
    //
    @Query("SELECT * FROM bike ORDER BY last_used_date DESC")
    fun observeBikesOrderedByLastUsedDate(): Flow<List<Bike>>

    @Query("SELECT * FROM activity ORDER BY title")
    fun observeActivitiesOrderedByTitle(): Flow<List<Activity>>

    @Query("SELECT * FROM activitywithbikedata ORDER BY activity_due_date DESC")
    fun observeActivitiesWithBikeDataOrderedByDueDate(): Flow<List<ActivityWithBikeData>>

    @Query("SELECT * FROM activitywithbikedata WHERE activity_due_date NOT NULL ORDER BY activity_due_date DESC")
    fun observeActivitiesWithBikeDataAndDueDateOrderedByDueDate(): Flow<List<ActivityWithBikeData>>

    @Query("SELECT * FROM templateactivity ORDER BY ride_level, title")
    fun observeTemplateActivities(): Flow<List<TemplateActivity>>

    @Query("SELECT * FROM component ORDER BY name")
    fun observeComponentsOrderedByName(): Flow<List<Component>>

    @Query("SELECT * FROM component WHERE retirement_date IS NULL ORDER BY name")
    fun observeNonRetiredComponentsOrderedByName(): Flow<List<Component>>

    @Query("SELECT * FROM retiredcomponents ORDER BY retirement_date DESC")
    fun observeRetiredComponents(): Flow<List<RetiredComponents>>

    @Query("SELECT * FROM automaticactivitiesgenerationlog ORDER BY created_instant DESC")
    fun observeAutomaticActivitiesGenerationLog(): Flow<List<AutomaticActivitiesGenerationLog>>

    //
    // GETTERS = return all lines
    //
    @Query("SELECT * FROM bike ORDER BY last_used_date DESC")
    suspend fun getAllBikesOrderedByLastUsedDate(): List<Bike>

    @Query("SELECT * FROM activity ORDER BY title")
    suspend fun getAllActivitiesOrderedByTitle(): List<Activity>

    @Query("SELECT * FROM templateactivity ORDER BY ride_level, title")
    suspend fun getAllTemplateActivities(): List<TemplateActivity>

    @Query("SELECT * FROM component ORDER BY name")
    suspend fun getAllComponents(): List<Component>

    //
    // GETTERS - return individual lines
    //
    @Query("SELECT * FROM activity WHERE uid=:uid")
    suspend fun getActivityByUid(uid: Long): Activity?

    @Query("SELECT * FROM bike WHERE uid=:uid")
    suspend fun getBikeByUid(uid: Long): Bike?

    @Query("SELECT * FROM templateactivity WHERE uid=:uid")
    suspend fun getTemplateActivityByUid(uid: Long): TemplateActivity?

    @Query("SELECT * FROM component WHERE uid=:uid")
    suspend fun getComponentByUid(uid: Long): Component?

    //
    // other helpers
    //
    @Query("SELECT COUNT(uid) FROM activity WHERE bike_uid=:bikeUid")
    suspend fun getActivityCountByBike(bikeUid: Long): Int

    @Query("SELECT * FROM templateactivity WHERE ride_level=:rideLevel")
    suspend fun getTemplateActivityForRideLevel(rideLevel: Int): List<TemplateActivity>

    //
    // UPDATE/INSERT/DELETE
    //
    @Insert
    suspend fun insertBike(bike: Bike): Long

    @Update
    suspend fun updateBike(bike: Bike)

    @Delete
    suspend fun deleteBike(bike: Bike)

    @Query("DELETE FROM bike")
    suspend fun deleteAllBikes()

    //

    @Insert
    suspend fun insertActivity(activity: Activity): Long

    @Update
    suspend fun updateActivity(activity: Activity)

    @Query("DELETE FROM Activity WHERE uid=:activityUid")
    suspend fun deleteActivityByUid(activityUid: Long)

    @Query("DELETE FROM Activity WHERE ride_uid=:rideUid")
    suspend fun deleteActivitiesForRide(rideUid: Long)

    @Query("DELETE FROM activity")
    suspend fun deleteAllActivities()

    //

    @Insert
    suspend fun insertRideUidByRideLevel(rideUidByRideLevel: RideUidByRideLevel): Long

    @Query("select * from RideUidByRideLevel WHERE ride_level=:rideLevel order by rowid desc LIMIT 1")
    suspend fun getLatestRideUidByRideLevel(rideLevel: Int): RideUidByRideLevel?

    //

    @Insert
    suspend fun insertTemplateActivity(templateActivity: TemplateActivity): Long

    @Update
    suspend fun updateTemplateActivity(templateActivity: TemplateActivity)

    @Query("DELETE FROM templateactivity WHERE uid=:uid")
    suspend fun deleteTemplateActivityByUid(uid: Long)

    @Query("DELETE FROM templateactivity")
    suspend fun deleteAllTemplateActivities()

    //

    @Insert
    suspend fun insertRide(ride: Ride): Long

    //

    @Insert
    suspend fun insertComponent(component: Component): Long

    @Query("DELETE FROM component")
    suspend fun deleteAllComponents()

    @Update
    suspend fun updateComponent(component: Component)

    @Delete
    suspend fun deleteComponent(component: Component)

    //

    @Insert
    suspend fun insertAAGLogEntry(logEntry: AutomaticActivitiesGenerationLog)

}