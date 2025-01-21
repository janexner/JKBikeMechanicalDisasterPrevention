package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface KJsDAO {
    @Query("SELECT * FROM activity ORDER BY due_date DESC")
    fun observeActivitiesOrderedByDueDate(): Flow<List<Activity>>

    @Update
    suspend fun updateActivity(activity: Activity)
}