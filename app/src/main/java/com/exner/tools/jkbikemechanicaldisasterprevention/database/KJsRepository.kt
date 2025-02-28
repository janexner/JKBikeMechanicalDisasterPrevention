package com.exner.tools.jkbikemechanicaldisasterprevention.database

import androidx.annotation.WorkerThread
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.AutomaticActivitiesGenerationLog
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Ride
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.RideUidByRideLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.RetiredComponents
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

    val observeComponents: Flow<List<Component>> = kjsDAO.observeComponentsOrderedByName()

    val observeNonRetiredComponents: Flow<List<Component>> = kjsDAO.observeNonRetiredComponentsOrderedByName()

    val observeRetiredComponents: Flow<List<RetiredComponents>> = kjsDAO.observeRetiredComponents()

    val observeAutomaticActivitiesGenerationLog: Flow<List<AutomaticActivitiesGenerationLog>> = kjsDAO.observeAutomaticActivitiesGenerationLog()

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
    suspend fun deleteAllActivities() {
        kjsDAO.deleteAllActivities()
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
    suspend fun deleteAllTemplateActivities() {
        kjsDAO.deleteAllTemplateActivities()
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
    suspend fun deleteAllBikes() {
        kjsDAO.deleteAllBikes()
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

    @WorkerThread
    suspend fun insertComponent(component: Component): Long {
        return kjsDAO.insertComponent(component)
    }

    @WorkerThread
    suspend fun getAllComponents(): List<Component> {
        return kjsDAO.getAllComponents()
    }

    @WorkerThread
    suspend fun getComponentByUid( uid: Long) : Component? {
        return kjsDAO.getComponentByUid(uid)
    }

    @WorkerThread
    suspend fun deleteAllComponents() {
        kjsDAO.deleteAllComponents()
    }

    @WorkerThread
    suspend fun updateComponent(component: Component) {
        kjsDAO.updateComponent(component)
    }

    @WorkerThread
    suspend fun deleteComponent(component: Component) {
        kjsDAO.deleteComponent(component)
    }

    @WorkerThread
    suspend fun insertAutomaticActivitiesCreationLog(logEntry: AutomaticActivitiesGenerationLog) {
        kjsDAO.insertAAGLogEntry(logEntry)
    }
}