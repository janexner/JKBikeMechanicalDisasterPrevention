package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.annotation.WorkerThread
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.BikeActivities
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivitiesByBikes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KJsRepository @Inject constructor(private val kjsDAO: KJsDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val observeBikes: Flow<List<Bike>> = kjsDAO.observeBikesOrderedByLastUsedDate()

    val observeActivitiesByBikes: Flow<List<ActivitiesByBikes>> = kjsDAO.observeActivitiesByBikes()

    val observeActivitiesByBikesWithDueDate: Flow<List<ActivitiesByBikes>> = kjsDAO.observeActivitiesByBikeWithDateOrderedByDueDate()

    val observeComponents: Flow<List<Component>> = kjsDAO.observeComponentsOrderedAlphabetically()

    val observeShelvedComponents: Flow<List<Component>> = kjsDAO.observeShelvedComponents()

    @WorkerThread
    suspend fun updateActivity(activity: Activity) {
        kjsDAO.updateActivity(activity)
    }

    @WorkerThread
    suspend fun getActivityCountByBike(bikeUid: Long): Int {
        return kjsDAO.getActivityCountByBike(bikeUid)
    }

    @WorkerThread
    suspend fun getActivitiesForBike(bikeUid: Long): List<ActivitiesByBikes> {
        return kjsDAO.getActivitiesForBike(bikeUid)
    }

    @WorkerThread
    suspend fun deleteActivityByUid(activityUid: Long) {
        kjsDAO.deleteActivityByUid(activityUid)
    }

    @WorkerThread
    suspend fun deleteBikeActivitiesByBike(bikeUid: Long) {
        kjsDAO.deleteBikeActivitiesByBike(bikeUid)
    }

    @WorkerThread
    suspend fun getBikeByUid(uid: Long) : Bike? {
        return kjsDAO.getBikeByUid(uid)
    }

    @WorkerThread
    suspend fun insertBike(bike: Bike): Long {
        return kjsDAO.insertBike(bike)
    }

    @WorkerThread
    suspend fun updateBike(bike: Bike) {
        kjsDAO.updateBike(bike)
    }

    @WorkerThread
    suspend fun deleteBike(bike: Bike) {
        kjsDAO.deleteBike(bike)
    }

    @WorkerThread
    suspend fun insertComponent(component: Component): Long {
        return kjsDAO.insertComponent(component)
    }

    @WorkerThread
    suspend fun getComponentByUid(uid: Long) : Component? {
        return kjsDAO.getComponentByUid(uid)
    }

    @WorkerThread
    suspend fun getComponentCountByBike(bikeUid: Long) : Int {
        return kjsDAO.getComponentCountByBike(bikeUid)
    }

    @WorkerThread
    suspend fun deleteComponentsForBike(bikeUid: Long) {
        kjsDAO.deleteComponentsForBike(bikeUid)
    }
}