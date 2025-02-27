package com.exner.tools.jkbikemechanicaldisasterprevention.scheduler

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

private const val TAG = "CCIACAWorker"

class CheckComponentsIntervalsAndCreateActivitiesWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
    val repository: KJsRepository
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Log.d(TAG, "Starting periodic check...")

        // Do the work here
        CoroutineScope(Dispatchers.IO).launch {
            val allComponents = repository.getAllComponents()
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            allComponents.forEach { component ->
                if (component.bikeUid != null) { // is attached to a bike
                    if (0 < (component.firstUseDate?.compareTo(today) ?: 0)) {
                        // first_use_date is in the past! This component is being used!
                        var isItTime = false
                        // check mileage
                        val bike = repository.getBikeByUid(component.bikeUid)
                        if (bike != null) {
                            if (component.checkIntervalMiles != null) {
                                if (bike.mileage > (component.checkIntervalMiles + (component.currentMileage ?: 0))) {
                                    isItTime = true
                                }
                            }
                        }
                        // check time
                        if (component.checkIntervalDays != null && component.lastCheckDate != null) {
                            if (component.lastCheckDate.plus(
                                    component.checkIntervalDays,
                                    DateTimeUnit.DAY
                                ) < today
                            ) {
                                isItTime = true
                            }
                        }
                        // if either of them were due, do it
                        if (isItTime) {
                            val title = component.titleForAutomaticActivities
                                ?: (appContext.getString(R.string.check_component) + " " + component.name)
                            val activity = Activity(
                                title = title,
                                description = component.description,
                                isCompleted = false,
                                bikeUid = component.bikeUid,
                                isEBikeSpecific = bike?.isElectric ?: false,
                                rideLevel = null,
                                rideUid = null,
                                createdInstant = Clock.System.now(),
                                dueDate = today.plus(3, DateTimeUnit.DAY),
                                doneInstant = null,
                                componentUid = component.uid,
                                uid = 0L,
                            )
                            Log.d(TAG, "Creating activity $activity for component $component...")
                            repository.insertActivity(activity)
                        }
                    }
                }
            }
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}

/**
 * When an activity is generated from here, it will receive the componentUid
 *
 * When such an activity is checked / ticked off
 * - component lastCheckDate is updated
 * - component currentMileage is updated from bike
 * - component wear is updated (UI needed!)
 * - is taken off bike and retireDate updated when wear is "dead"
 */