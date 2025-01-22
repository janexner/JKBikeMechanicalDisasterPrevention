package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.annotation.WorkerThread
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KJsRepository @Inject constructor(val kjsDAO: KJsDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val observeActivitiesByDueDate: Flow<List<Activity>> = kjsDAO.observeActivitiesOrderedByDueDate()

    val observeBikes: Flow<List<Bike>> = kjsDAO.observeBikesOrderedByLastUsedDate()

    @WorkerThread
    suspend fun updateActivity(activity: Activity) {
        kjsDAO.updateActivity(activity)
    }

}