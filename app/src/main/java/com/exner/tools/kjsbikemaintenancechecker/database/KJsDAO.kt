package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.entities.ComponentActivities
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Ride
import com.exner.tools.kjsbikemaintenancechecker.database.entities.RideUidByRideLevel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.TemplateActivity
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivityWithBikeData
import kotlinx.coroutines.flow.Flow

@Dao
interface KJsDAO {
    //
    // OBSERVERS - return tables
    //
    @Query("SELECT * FROM bike ORDER BY last_used_date DESC")
    fun observeBikesOrderedByLastUsedDate(): Flow<List<Bike>>

    @Query("SELECT * FROM component ORDER BY name")
    fun observeComponentsOrderedAlphabetically(): Flow<List<Component>>

    @Query("SELECT * FROM activitywithbikedata ORDER BY activity_due_date DESC")
    fun observeActivitiesWithBikeDataOrderedByDueDate(): Flow<List<ActivityWithBikeData>>

    @Query("SELECT * FROM activitywithbikedata WHERE activity_due_date NOT NULL ORDER BY activity_due_date DESC")
    fun observeActivitiesWithBikeDataAndDueDateOrderedByDueDate(): Flow<List<ActivityWithBikeData>>

    @Query("SELECT * FROM shelvedcomponents ORDER BY name")
    fun observeShelvedComponents(): Flow<List<Component>>

    //
    // GETTERS - return individual lines
    //
    @Query("SELECT * FROM activity WHERE uid=:uid")
    suspend fun getActivityByUid(uid: Long): Activity?

    @Query("SELECT * FROM bike WHERE uid=:uid")
    suspend fun getBikeByUid(uid: Long): Bike?

    @Query("SELECT * FROM component WHERE uid=:uid")
    suspend fun getComponentByUid(uid: Long): Component?

    //
    // other helpers
    //
    @Query("SELECT COUNT(uid) FROM component WHERE bike_uid=:bikeUid")
    suspend fun getComponentCountByBike(bikeUid: Long): Int

    @Query("SELECT COUNT(uid) FROM component WHERE parent_component_uid=:parentUid")
    suspend fun getComponentCountByParent(parentUid: Long): Int

    @Query("SELECT COUNT(uid) FROM activity WHERE bike_uid=:bikeUid")
    suspend fun getActivityCountByBike(bikeUid: Long): Int

    @Query("SELECT * FROM activity WHERE bike_uid=:bikeUid")
    suspend fun getActivitiesForBike(bikeUid: Long): List<Activity>

    @Query("SELECT * FROM activitywithbikedata WHERE activity_ride_uid=:rideUid ORDER BY activity_due_date DESC")
    suspend fun getActivitiesWithBikeDataForRide(rideUid: Long): List<ActivityWithBikeData>

    @Query("SELECT * FROM templateactivity WHERE ride_level=:rideLevel ORDER BY due_date")
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

    //

    @Insert
    suspend fun insertComponent(component: Component): Long

    @Update
    suspend fun updateComponent(component: Component)

    @Delete
    suspend fun deleteComponent(component: Component)

    @Query("DELETE FROM component WHERE bike_uid=:bikeUid")
    suspend fun deleteComponentsForBike(bikeUid: Long)

    @Query("DELETE FROM component WHERE parent_component_uid=:parentUid")
    suspend fun deleteComponentsForParent(parentUid: Long)

    //

    @Insert
    suspend fun insertActivity(activity: Activity): Long

    @Insert
    suspend fun insertComponentActivity(componentActivities: ComponentActivities): Long

    @Update
    suspend fun updateActivity(activity: Activity)

    @Query("DELETE FROM Activity WHERE uid=:activityUid")
    suspend fun deleteActivityByUid(activityUid: Long)

    @Query("DELETE FROM Activity WHERE ride_uid=:rideUid")
    suspend fun deleteActivitiesForRide(rideUid: Long)

    //

    @Insert
    suspend fun insertRideUidByRideLevel(rideUidByRideLevel: RideUidByRideLevel): Long

    @Query("select * from RideUidByRideLevel WHERE ride_level=:rideLevel order by rowid desc LIMIT 1")
    suspend fun getLatestRideUidByRideLevel(rideLevel: Int): RideUidByRideLevel?

    //

    @Insert
    suspend fun insertTemplateActivity(templateActivity: TemplateActivity): Long

    //

    @Insert
    suspend fun insertRide(ride: Ride): Long
}