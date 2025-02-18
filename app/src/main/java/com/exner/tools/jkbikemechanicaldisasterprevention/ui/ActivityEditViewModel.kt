package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@HiltViewModel(assistedFactory = ActivityEditViewModel.ActivityEditViewModelFactory::class)
class ActivityEditViewModel @AssistedInject constructor(
    @Assisted val activityUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _activity: MutableLiveData<Activity> = MutableLiveData()
    val activity: LiveData<Activity> = _activity

    val observeBikes = repository.observeBikes
    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike

    init {
        if (activityUid > 0) {
            viewModelScope.launch {
                _activity.value = repository.getActivityByUid(activityUid)
            }
        }
    }

    fun updateTitle(title: String) {
        if (activity.value != null) {
            _activity.value = activity.value!!.copy(
                title = title
            )
        }
    }

    fun updateDescription(description: String) {
        if (activity.value != null) {
            _activity.value = activity.value!!.copy(
                description = description
            )
        }
    }

    fun updateIsCompleted(isCompleted: Boolean) {
        if (activity.value != null) {
            _activity.value = activity.value!!.copy(
                isCompleted = isCompleted
            )
        }
    }

    fun updateIsEBikeSpecific(isEBikeSpecific: Boolean) {
        if (activity.value != null) {
            _activity.value = activity.value!!.copy(
                isEBikeSpecific = isEBikeSpecific
            )
        }
    }

    fun updateCreatedDate(millis: Long) {
        if (activity.value != null) {
            val buildDateInstant = Instant.fromEpochMilliseconds(millis)
            _activity.value = activity.value!!.copy(
                createdInstant = buildDateInstant
            )
        }
    }

    fun updateDueDate(millis: Long?) {
        if (activity.value != null) {
            if (millis != null) {
                val buildDateInstant = Instant.fromEpochMilliseconds(millis)
                val buildDate =
                    buildDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                _activity.value = activity.value!!.copy(
                    dueDate = buildDate
                )
            } else {
                _activity.value = activity.value!!.copy(
                    dueDate = null
                )
            }
        }
    }

    fun updateAttachedBike(bikeUid: Long?) {
        if (bikeUid != null) {
            viewModelScope.launch {
                _currentBike.value = repository.getBikeByUid(bikeUid)
            }
        } else {
            _currentBike.value = null
        }
    }

    fun updateRideLevel(level: Int?) {
        viewModelScope.launch {
            _activity.value = activity.value!!.copy(
                rideLevel = level
            )
        }
    }

    fun commitActivity() {
        if (activity.value != null) {
            viewModelScope.launch {
                repository.updateActivity(activity.value!!)
            }
        }
    }

    @AssistedFactory
    interface ActivityEditViewModelFactory {
        fun create(activityUid: Long): ActivityEditViewModel
    }
}