package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Ride
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.RideUidByRideLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.preferences.UserPreferencesManager
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.RideLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Duration

private const val TAG = "PrepareDayOutVM"

@HiltViewModel
class PrepareDayOutViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val repository: KJsRepository
) : ViewModel() {

    val observeBikesRaw = repository.observeBikes

    val observeActivitiesByBikes = repository.observeActivityWithBikeDataAndDueDateOrderedByDueDate

    private val rideLevelDayOut = MutableStateFlow(RideLevel.getRideLevelDayOut())

    val rideUid = MutableStateFlow(0L)

    fun updateActivity(activity: Activity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
    }

    val observeActivitiesDayOut: StateFlow<List<ActivityWithBikeData>> =
        repository.observeActivityWithBikeDataOrderedByDueDate
            .combine(rideUid) { result, rideUid ->
                result.filter { activityWithBikeData ->
                    activityWithBikeData.rideUid == rideUid // short ride
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    fun updateRideActivity(activityUid: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            val activity = repository.getActivityByUid(activityUid)
            if (activity != null) {
                activity.isCompleted = isCompleted
                repository.updateActivity(activity)
            }
        }
    }

    private val _showIntroText = mutableStateOf(true)
    val showIntroText: State<Boolean> = _showIntroText

    fun updateShowIntroText(show: Boolean) {
        _showIntroText.value = show
    }

    fun endCurrentRideAndStartFromScratch() {
        // delete the old ride
        viewModelScope.launch {
            repository.deleteActivitiesForRide(rideUid = rideUid.value)
        }
        // then create a new one
        viewModelScope.launch {
            rideUid.value = copyTemplateActivities(force = true)
        }
    }

    init {
        // copy short trip template activities
        viewModelScope.launch {
            rideUid.value = copyTemplateActivities()
        }
        // remove intro text in 2s
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            _showIntroText.value = false
        }, 2000)
    }

    private suspend fun copyTemplateActivities(force: Boolean = false): Long {
        Log.d(TAG, "Will copy template activities...")
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val currentDayOut = Ride(
            name = "Day Out $today",
            level = RideLevel.getRideLevelDayOut(),
            createdInstant = Clock.System.now()
        )
        var potentialOldRide = repository.getLatestRideUidByRideLevel(rideLevelDayOut.value)
        Log.d(TAG, "Found potential old ride ${potentialOldRide?.rideUid}")
        val oldTodoListsExpire = userPreferencesManager.todoListsExpire().firstOrNull()
        Log.d(TAG, "User wishes old rides to expire after 2 days")
        if ((force || oldTodoListsExpire == true) && potentialOldRide != null) {
            val now = Clock.System.now()
            val age: Duration = now - potentialOldRide.createdInstant
            Log.d(TAG, "Potential old ride age is $age (${age.inWholeDays} days)")
            if (force || (age.isPositive() && age.inWholeDays > 2)) {
                // the one we found is old, so lets ditch it
                Log.d(TAG, "Deleting old ride ${potentialOldRide.rideUid}")
                repository.deleteActivitiesForRide(potentialOldRide.rideUid)
                potentialOldRide = null
            }
        }
        if (force || potentialOldRide == null) {
            val newRideUid: Long = repository.insertRide(currentDayOut)
            val rideUidByRideLevel = RideUidByRideLevel(
                rideUid = newRideUid,
                rideLevel = rideLevelDayOut.value,
                createdInstant = Clock.System.now(),
                uid = 0
            )
            Log.d(TAG, "Adding ride to repository ${rideUidByRideLevel.uid}")
            repository.insertRideUidByRideLevel(rideUidByRideLevel = rideUidByRideLevel)
            Log.d(TAG, "Going to create activities from template activities...")
            val rideLevelDayOut = RideLevel.getRideLevelDayOut()
            repository.getTemplateActivityForRideLevel(rideLevel = rideLevelDayOut)
                .forEach { templateActivity ->
                    val activity = Activity(
                        title = templateActivity.title,
                        description = templateActivity.description,
                        isCompleted = false,
                        bikeUid = null,
                        isEBikeSpecific = templateActivity.isEBikeSpecific,
                        rideLevel = templateActivity.rideLevel ?: rideLevelDayOut,
                        rideUid = newRideUid,
                        createdInstant = Clock.System.now(),
                        dueDate = null,
                        doneInstant = null,
                        uid = 0,
                    )
                    Log.d(TAG, "Adding activity ${activity.title}...")
                    repository.insertActivity(activity = activity)
                }
            Log.d(TAG, "Done adding activities.")
            return newRideUid
        }
        return potentialOldRide.rideUid
    }
}