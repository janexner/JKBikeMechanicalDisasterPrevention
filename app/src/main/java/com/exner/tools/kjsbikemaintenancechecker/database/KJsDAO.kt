package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Dao
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
    @Query("SELECT * FROM activity ORDER BY due_date DESC")
    fun observeActivitiesOrderedByDueDate(): Flow<List<Activity>>

    @Query("SELECT * FROM bike ORDER BY last_used_date DESC")
    fun observeBikesOrderedByLastUsedDate(): Flow<List<Bike>>

    @Query("SELECT * FROM activitiesbybikes")
    fun observeActivitiesByBikes(): Flow<List<ActivitiesByBikes>>

    @Query("SELECT * FROM component ORDER BY name")
    fun observeComponentsOrderedAlphabetically(): Flow<List<Component>>

    @Query("SELECT * FROM bike WHERE uid=:uid")
    suspend fun getBikeByUid(uid: Long): Bike?

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

    @Insert
    suspend fun insertComponentActivity(componentActivities: ComponentActivities)
}