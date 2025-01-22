package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.BikeActivities
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import kotlinx.coroutines.flow.Flow

@Dao
interface KJsDAO {
    @Query("SELECT * FROM activity ORDER BY due_date DESC")
    fun observeActivitiesOrderedByDueDate(): Flow<List<Activity>>

    @Query("SELECT * FROM bike ORDER BY last_used_date DESC")
    fun observeBikesOrderedByLastUsedDate(): Flow<List<Bike>>

    @Update
    suspend fun updateActivity(activity: Activity)

    @Insert
    suspend fun insertBike(bike: Bike)

    @Insert
    suspend fun insertComponent(component: Component)

    @Insert
    suspend fun insertActivity(activity: Activity)

    @Insert
    suspend fun insertBikeActivities(bikeActivities: BikeActivities)
}