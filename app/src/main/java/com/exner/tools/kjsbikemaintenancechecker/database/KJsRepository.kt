package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.annotation.WorkerThread
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivitiesByBikes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KJsRepository @Inject constructor(private val kjsDAO: KJsDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val observeActivitiesByDueDate: Flow<List<Activity>> = kjsDAO.observeActivitiesOrderedByDueDate()

    val observeBikes: Flow<List<Bike>> = kjsDAO.observeBikesOrderedByLastUsedDate()

    val observeActivitiesByBikes: Flow<List<ActivitiesByBikes>> = kjsDAO.observeActivitiesByBikes()

    val observeComponents: Flow<List<Component>> = kjsDAO.observeComponentsOrderedAlphabetically()

    @WorkerThread
    suspend fun updateActivity(activity: Activity) {
        kjsDAO.updateActivity(activity)
    }

    @WorkerThread
    suspend fun getBikeByUid(uid: Long) : Bike? {
        return kjsDAO.getBikeByUid(uid)
    }

}