package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
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

    val observeComponents = repository.observeComponents
    private val _currentParentComponent: MutableStateFlow<Component?> = MutableStateFlow(null)
    val currentParentComponent: StateFlow<Component?> = _currentParentComponent

    fun updateName(name: String) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                name = name
            )
        }
    }

    fun updateDescription(description: String) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                description = description
            )
        }
    }

    fun updateMileage(mileage: Int) {
        if (component.value != null) {
            _component.value = component.value!!.copy(
                mileage = mileage
            )
        }
    }

    fun updateAcquisitionDate(millis: Long) {
        if (component.value != null) {
            val buildDateInstant = Instant.fromEpochMilliseconds(millis)
            val buildDate =
                buildDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            _component.value = component.value!!.copy(
                acquisitionDate = buildDate
            )
        }
    }

    fun updateLastUsedDate(millis: Long?) {
        if (component.value != null) {
            if (millis != null) {
                val lastUsedDateInstant = Instant.fromEpochMilliseconds(millis)
                val lastUsedDate =
                    lastUsedDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                _component.value = component.value!!.copy(
                    lastUsedDate = lastUsedDate
                )
            } else {
                _component.value = component.value!!.copy(
                    lastUsedDate = null
                )
            }
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
                if (component.value != null) {
                    _currentBike.value = repository.getBikeByUid(component.value!!.bikeUid)
                    if (component.value!!.parentComponentUid != null) {
                        _currentParentComponent.value =
                            repository.getComponentByUid(component.value!!.parentComponentUid!!)
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface ComponentEditViewModelFactory {
        fun create(componentUid: Long): ComponentEditViewModel
    }
}