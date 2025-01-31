package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.annotation.WorkerThread
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivityWithBikeData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KJsRepository @Inject constructor(private val kjsDAO: KJsDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val observeBikes: Flow<List<Bike>> = kjsDAO.observeBikesOrderedByLastUsedDate()

    val observeActivityWithBikeDataOrderedByDueDate: Flow<List<ActivityWithBikeData>> =
        kjsDAO.observeActivitiesByBikeWithDateOrderedByDueDate()

    val observeComponents: Flow<List<Component>> = kjsDAO.observeComponentsOrderedAlphabetically()

    val observeShelvedComponents: Flow<List<Component>> = kjsDAO.observeShelvedComponents()

    @WorkerThread
    suspend fun getActivityByUid(activityUid: Long): Activity? {
        return kjsDAO.getActivityByUid(activityUid)
    }

    @WorkerThread
    suspend fun insertActivity(activity: Activity) {
        kjsDAO.insertActivity(activity)
    }

    @WorkerThread
    suspend fun updateActivity(activity: Activity) {
        kjsDAO.updateActivity(activity)
    }

    @WorkerThread
    suspend fun getActivityCountByBike(bikeUid: Long): Int {
        return kjsDAO.getActivityCountByBike(bikeUid)
    }

    @WorkerThread
    suspend fun getActivitiesForBike(bikeUid: Long): List<Activity> {
        return kjsDAO.getActivitiesForBike(bikeUid)
    }

    @WorkerThread
    suspend fun deleteActivityByUid(activityUid: Long) {
        kjsDAO.deleteActivityByUid(activityUid)
    }

    @WorkerThread
    suspend fun getBikeByUid(uid: Long): Bike? {
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
    suspend fun updateComponent(component: Component) {
        kjsDAO.updateComponent(component)
    }

    @WorkerThread
    suspend fun getComponentByUid(uid: Long): Component? {
        return kjsDAO.getComponentByUid(uid)
    }

    @WorkerThread
    suspend fun getComponentCountByBike(bikeUid: Long): Int {
        return kjsDAO.getComponentCountByBike(bikeUid)
    }

    @WorkerThread
    suspend fun deleteComponentsForBike(bikeUid: Long) {
        kjsDAO.deleteComponentsForBike(bikeUid)
    }

    @WorkerThread
    suspend fun getComponentCountByParent(parentUid: Long): Int {
        return kjsDAO.getComponentCountByParent(parentUid)
    }

    @WorkerThread
    suspend fun deleteComponentsForParent(parentUid: Long) {
        kjsDAO.deleteComponentsForParent(parentUid)
    }

    @WorkerThread
    suspend fun deleteComponent(component: Component) {
        kjsDAO.deleteComponent(component)
    }
}