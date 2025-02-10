package com.exner.tools.jkbikemechanicaldisasterprevention.database

import androidx.annotation.WorkerThread
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Accessory
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Ride
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.RideUidByRideLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.RideLevel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KJsRepository @Inject constructor(private val kjsDAO: KJsDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val observeBikes: Flow<List<Bike>> = kjsDAO.observeBikesOrderedByLastUsedDate()

    val observeActivityWithBikeDataOrderedByDueDate: Flow<List<ActivityWithBikeData>> =
        kjsDAO.observeActivitiesWithBikeDataOrderedByDueDate()

    val observeActivityWithBikeDataAndDueDateOrderedByDueDate: Flow<List<ActivityWithBikeData>> =
        kjsDAO.observeActivitiesWithBikeDataAndDueDateOrderedByDueDate()

    val observeComponents: Flow<List<Component>> = kjsDAO.observeComponentsOrderedAlphabetically()

    val observeAccessories: Flow<List<Accessory>> = kjsDAO.observeAccessoriesOrderedAlphabetically()

    val observeActivities: Flow<List<Activity>> = kjsDAO.observeActivitiesOrderedByTitle()

    val observeShelvedComponents: Flow<List<Component>> = kjsDAO.observeShelvedComponents()

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
    suspend fun getActivityCountByBike(bikeUid: Long): Int {
        return kjsDAO.getActivityCountByBike(bikeUid)
    }

    @WorkerThread
    suspend fun getActivitiesForBike(bikeUid: Long): List<Activity> {
        return kjsDAO.getActivitiesForBike(bikeUid)
    }

    @WorkerThread
    suspend fun getActivityWithBikeDataForRide(rideUid: Long): List<ActivityWithBikeData> {
        return kjsDAO.getActivitiesWithBikeDataForRide(rideUid)
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
    suspend fun getTemplateActivityForRideLevel(rideLevel: RideLevel): List<TemplateActivity> {
        return kjsDAO.getTemplateActivityForRideLevel(rideLevel)
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
    suspend fun getComponentsForBike(bikeUid: Long): List<Component> {
        return kjsDAO.getComponentsForBike(bikeUid)
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

    @WorkerThread
    suspend fun getAllComponents(): List<Component> {
        return kjsDAO.getAllComponentsOrderedByBike()
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
    suspend fun insertAccessory(accessory: Accessory): Long {
        return kjsDAO.insertAccessory(accessory)
    }

    @WorkerThread
    suspend fun updateAccessory(accessory: Accessory) {
        kjsDAO.updateAccessory(accessory)
    }

    @WorkerThread
    suspend fun getAccessoryByUid(accessoryUid: Long): Accessory? {
        return kjsDAO.getAccessoryByUid(accessoryUid)
    }

    @WorkerThread
    suspend fun getAccessoryCountByParent(parentUid: Long): Int {
        return kjsDAO.getAccessoryCountByParent(parentUid)
    }

    @WorkerThread
    suspend fun deleteAccessoriesForParent(parentUid: Long) {
        kjsDAO.deleteAccessoriesForParent(parentUid)
    }

    @WorkerThread
    suspend fun deleteAccessory(accessory: Accessory) {
        kjsDAO.deleteAccessory(accessory)
    }

    @WorkerThread
    suspend fun getAllAccessories(): List<Accessory> {
        return kjsDAO.getAllAccessoriesOrderedByName()
    }

}