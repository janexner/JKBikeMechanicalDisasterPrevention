package com.exner.tools.kjsbikemaintenancechecker.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject

@HiltViewModel
class PrepareShortRideViewModel @Inject constructor(
    private val repository: KJsRepository
) : ViewModel() {

    val observeBikesRaw = repository.observeBikes

    val observeActivitiesByBikes = repository.observeActivityWithBikeDataAndDueDateOrderedByDueDate

    fun updateActivity(activity: Activity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
    }

    private var _shortRideActivities: MutableList<Activity> = mutableListOf()
    val shortRideActivities: List<Activity> = _shortRideActivities

    fun updateRideActivity(index: Int, isCompleted: Boolean) {
        _shortRideActivities[index] = shortRideActivities[index].copy(
            isCompleted = isCompleted,
            doneDate = if (isCompleted) { Clock.System.now() } else { null }
        )
    }

    private val _showIntroText = mutableStateOf(true)
    val showIntroText: State<Boolean> = _showIntroText

    fun updateShowIntroText(show: Boolean) {
        _showIntroText.value = show
    }

    init {
        val nowDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

        // create activities for short ride
        val tyrePressureActivity = Activity(
            title = "Check/fix tyre pressure",
            description = "Check pressure on both tyres, and inflate if necessary.",
            isCompleted = false,
            bikeUid = 0,
            createdDate = nowDate,
            dueDate = null,
            doneDate = null,
            uid = 0,
        )
        _shortRideActivities.add(tyrePressureActivity)
        val checkForKnocksActivity = Activity(
            title = "Brake knock test",
            description = "Pull both brakes and rock bike back and forth to test for knocks",
            isCompleted = false,
            bikeUid = 0,
            createdDate = nowDate,
            dueDate = null,
            doneDate = null,
            uid = 1,
        )
        _shortRideActivities.add(checkForKnocksActivity)
        val checkAxlesActivity = Activity(
            title = "Check axles",
            description = "Check that both axles are properly installed and torqued",
            isCompleted = false,
            bikeUid = 0,
            createdDate = nowDate,
            dueDate = null,
            doneDate = null,
            uid = 2
        )
        _shortRideActivities.add(checkAxlesActivity)
        val rearMechAttachedActivity = Activity(
            title = "Check rear derailleur",
            description = "Check that the rear mech is securely attached and not bent or damaged",
            isCompleted = false,
            bikeUid = 0,
            createdDate = nowDate,
            dueDate = null,
            doneDate = null,
            uid = 3
        )
        _shortRideActivities.add(rearMechAttachedActivity)
        val dropperWorksActivity = Activity(
            title = "Check dropper post",
            description = "Check that dropper post moves up, down, and stays where it should",
            isCompleted = false,
            bikeUid = 0,
            createdDate = nowDate,
            dueDate = null,
            doneDate = null,
            uid = 4
        )
        _shortRideActivities.add(dropperWorksActivity)
        val gearsIndexedActivity = Activity(
            title = "Check gears",
            description = "Check that gears are indexed properly and all gears can be used",
            isCompleted = false,
            bikeUid = 0,
            createdDate = nowDate,
            dueDate = null,
            doneDate = null,
            uid = 5
        )
        _shortRideActivities.add(gearsIndexedActivity)

        // remove intro text in 2s
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            _showIntroText.value = false
        }, 2000)
    }
}