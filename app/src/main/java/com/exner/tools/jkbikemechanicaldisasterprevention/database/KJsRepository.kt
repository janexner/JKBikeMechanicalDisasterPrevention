package com.exner.tools.jkbikemechanicaldisasterprevention.database

import androidx.annotation.WorkerThread
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Ride
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.RideUidByRideLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KJsRepository @Inject constructor(private val kjsDAO: KJsDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val observeBikes: Flow<List<Bike>> = kjsDAO.observeBikesOrderedByLastUsedDate()

    val observeActivities: Flow<List<Activity>> = kjsDAO.observeActivitiesOrderedByTitle()

    val observeActivityWithBikeDataOrderedByDueDate: Flow<List<ActivityWithBikeData>> =
        kjsDAO.observeActivitiesWithBikeDataOrderedByDueDate()

    val observeActivityWithBikeDataAndDueDateOrderedByDueDate: Flow<List<ActivityWithBikeData>> =
        kjsDAO.observeActivitiesWithBikeDataAndDueDateOrderedByDueDate()

    val observeTemplateActivity: Flow<List<TemplateActivity>> = kjsDAO.observeTemplateActivities()

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
    suspend fun deleteActivityByUid(activityUid: Long) {
        kjsDAO.deleteActivityByUid(activityUid)
    }

    @WorkerThread
    suspend fun deleteActivitiesForRide(rideUid: Long) {
        kjsDAO.deleteActivitiesForRide(rideUid)
    }

    @WorkerThread
    suspend fun getAllActivities(): List<Activity> {
        return kjsDAO.getAllActivitiesOrderedByTitle()
    }

    @WorkerThread
    suspend fun getTemplateActivity(templateActivityUid: Long): TemplateActivity? {
        return kjsDAO.getTemplateActivityByUid(templateActivityUid)
    }

    @WorkerThread
    suspend fun getTemplateActivityForRideLevel(rideLevel: Int): List<TemplateActivity> {
        return kjsDAO.getTemplateActivityForRideLevel(rideLevel)
    }

    @WorkerThread
    suspend fun getTemplateActivityByUid(uid: Long): TemplateActivity? {
        return kjsDAO.getTemplateActivityByUid(uid)
    }

    @WorkerThread
    suspend fun insertTemplateActivity(templateActivity: TemplateActivity) {
        kjsDAO.insertTemplateActivity(templateActivity)
    }

    @WorkerThread
    suspend fun updateTemplateActivity(templateActivity: TemplateActivity) {
        kjsDAO.updateTemplateActivity(templateActivity)
    }

    @WorkerThread
    suspend fun deleteTemplateActivityByUid(templateActivityUid: Long) {
        kjsDAO.deleteTemplateActivityByUid(templateActivityUid)
    }

    @WorkerThread
    suspend fun getAllTemplateActivities(): List<TemplateActivity> {
        return kjsDAO.getAllTemplateActivities()
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
    suspend fun getAllBikes(): List<Bike> {
        return kjsDAO.getAllBikesOrderedByLastUsedDate()
    }

    @WorkerThread
    suspend fun insertRide(ride: Ride): Long {
        return kjsDAO.insertRide(ride)
    }

    @WorkerThread
    suspend fun insertRideUidByRideLevel(rideUidByRideLevel: RideUidByRideLevel): Long {
        return kjsDAO.insertRideUidByRideLevel(rideUidByRideLevel)
    }

    @WorkerThread
    suspend fun getLatestRideUidByRideLevel(rideLevel: Int): RideUidByRideLevel? {
        return kjsDAO.getLatestRideUidByRideLevel(rideLevel)
    }

}