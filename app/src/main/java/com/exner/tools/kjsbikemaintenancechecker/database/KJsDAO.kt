package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KJsDAO {
    @Query("SELECT * FROM activity ORDER BY due_date DESC")
    fun observeActivitiesOrderedByDueDate(): Flow<List<Activity>>
}