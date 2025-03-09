package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.RetirementReason
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel
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

@HiltViewModel(assistedFactory = ComponentEditViewModel.ComponentEditViewModelFactory::class)
class ComponentEditViewModel @AssistedInject constructor(
    @Assisted val componentUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _component: MutableLiveData<Component?> = MutableLiveData()
    val component: LiveData<Component?> = _component

    val observeBikes = repository.observeBikes
    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike

    fun updateName(newName: String) {
        if (component.value != null) {
            _component.value = component.value?.copy(
                name = newName
            )
        }
    }

    fun updateDescription(newDescription: String) {
        if (component.value != null) {
            _component.value = component.value?.copy(
                description = newDescription
            )
        }
    }

    fun updateAcquisitionDate(millis: Long?) {
        if (component.value != null) {
            if (millis != null) {
                val acquisitionDateInstant = Instant.fromEpochMilliseconds(millis)
                val acquisitionDate =
                    acquisitionDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                _component.value = component.value!!.copy(
                    acquisitionDate = acquisitionDate
                )
            } else {
                _component.value = component.value!!.copy(
                    acquisitionDate = null
                )
            }
        }
    }

    fun updateLastCheckDate(millis: Long?) {
        if (component.value != null) {
            if (millis != null) {
                val lastCheckDateInstant = Instant.fromEpochMilliseconds(millis)
                val lastCheckDate =
                    lastCheckDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                _component.value = component.value!!.copy(
                    lastCheckDate = lastCheckDate
                )
            } else {
                _component.value = component.value!!.copy(
                    lastCheckDate = null
                )
            }
        }
    }

    fun updateLastCheckMileage(mileage: Int) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                lastCheckMileage = mileage
            )
        }
    }

    fun updateCurrentMileage(mileage: Int) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                currentMileage = mileage
            )
        }
    }

    fun updateFirstUsedDate(millis: Long?) {
        if (component.value != null) {
            if (millis != null) {
                val firstUsedDateInstant = Instant.fromEpochMilliseconds(millis)
                val firstUsedDate =
                    firstUsedDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                _component.value = component.value!!.copy(
                    firstUseDate = firstUsedDate
                )
            } else {
                _component.value = component.value!!.copy(
                    firstUseDate = null
                )
            }
        }
    }

    fun updateBike(bikeUid: Long?) {
        if (bikeUid != null) {
            viewModelScope.launch {
                _currentBike.value = repository.getBikeByUid(bikeUid)
            }
        } else {
            _currentBike.value = null
        }
    }

    fun updateTitleForAutomaticActivities(title: String) {
        if (component.value != null) {
            _component.value = component.value?.copy(
                titleForAutomaticActivities = title
            )
        }
    }

    fun updateWearLevel(wearLevel: WearLevel?) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                wearLevel = wearLevel
            )
        }
    }

    fun updateRetirementDate(millis: Long?) {
        if (component.value != null) {
            if (millis != null) {
                val retirementDateInstant = Instant.fromEpochMilliseconds(millis)
                val retirementDate = retirementDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                _component.value = component.value!!.copy(
                    retirementDate = retirementDate
                )
            } else {
                _component.value = component.value!!.copy(
                    retirementDate = null
                )
            }
        }
    }

    fun updateRetirementReason(reason: RetirementReason?) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                retirementReason = reason
            )
        }
    }

    fun updateCheckIntervalMileage(mileage: Int) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                checkIntervalMiles = mileage
            )
        }
    }

    fun updateCheckIntervalDays(days: Int) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                checkIntervalDays = days
            )
        }
    }

    fun commitComponent() {
        if (component.value != null) {
            viewModelScope.launch {
                repository.updateComponent(component.value!!)
            }
        }
    }

    init {
        if (componentUid > 0) {
            viewModelScope.launch {
                _component.value = repository.getComponentByUid(componentUid)
            }
        }
    }

    @AssistedFactory
    interface ComponentEditViewModelFactory {
        fun create(componentUid: Long): ComponentEditViewModel
    }
}
