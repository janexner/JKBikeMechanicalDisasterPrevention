package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.BikeActivities
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.entities.ComponentActivities
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivitiesByBikes
import kotlinx.coroutines.flow.Flow

@Dao
interface KJsDAO {
    //
    // OBSERVERS - return tables
    //
    @Query("SELECT * FROM activity ORDER BY due_date DESC")
    fun observeActivitiesOrderedByDueDate(): Flow<List<Activity>>

    @Query("SELECT * FROM bike ORDER BY last_used_date DESC")
    fun observeBikesOrderedByLastUsedDate(): Flow<List<Bike>>

    @Query("SELECT * FROM activitiesbybikes")
    fun observeActivitiesByBikes(): Flow<List<ActivitiesByBikes>>

    @Query("SELECT * FROM component ORDER BY name")
    fun observeComponentsOrderedAlphabetically(): Flow<List<Component>>

    @Query("SELECT * FROM activitiesbybikes WHERE activity_due_date NOT NULL ORDER BY activity_due_date DESC")
    fun observeActivitiesByBikeWithDateOrderedByDueDate(): Flow<List<ActivitiesByBikes>>

    //
    // GETTERS - return individual lines
    //
    @Query("SELECT * FROM bike WHERE uid=:uid")
    suspend fun getBikeByUid(uid: Long): Bike?

    @Query("SELECT * FROM component WHERE uid=:uid")
    suspend fun getComponentByUid(uid: Long): Component?

    //
    // other helpers
    //
    @Query("SELECT COUNT(uid) FROM component WHERE bike_uid=:bikeUid")
    suspend fun getComponentCountByBike(bikeUid: Long): Int

    @Query("SELECT COUNT(activity_uid) FROM activitiesbybikes WHERE bike_uid=:bikeUid")
    suspend fun getActivityCountByBike(bikeUid: Long): Int

    @Query("SELECT * FROM ActivitiesByBikes WHERE bike_uid=:bikeUid")
    suspend fun getActivitiesForBike(bikeUid: Long): List<ActivitiesByBikes>

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

    @Query("DELETE FROM component WHERE bike_uid=:bikeUid")
    suspend fun deleteComponentsForBike(bikeUid: Long)

    //

    @Insert
    suspend fun insertActivity(activity: Activity): Long

    @Insert
    suspend fun insertBikeActivities(bikeActivities: BikeActivities): Long

    @Insert
    suspend fun insertComponentActivity(componentActivities: ComponentActivities): Long

    @Update
    suspend fun updateActivity(activity: Activity)

    @Query("DELETE FROM Activity WHERE uid=:activityUid")
    suspend fun deleteActivityByUid(activityUid: Long)

    @Query("DELETE FROM BikeActivities WHERE bike_uid=:bikeUid")
    suspend fun deleteBikeActivitiesByBike(bikeUid: Long)

}