package com.exner.tools.kjdoitnow.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjdoitnow.database.KJsRepository
import com.exner.tools.kjdoitnow.database.entities.Accessory
import com.exner.tools.kjdoitnow.database.entities.Bike
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

@HiltViewModel(assistedFactory = AccessoryEditViewModel.AccessoryEditViewModelFactory::class)
class AccessoryEditViewModel @AssistedInject constructor(
    @Assisted val accessoryUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _accessory: MutableLiveData<Accessory?> = MutableLiveData()
    val accessory: LiveData<Accessory?> = _accessory

    val observeBikes = repository.observeBikes
    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike

    val observeAccessories = repository.observeAccessories
    private val _currentParentAccessory: MutableStateFlow<Accessory?> = MutableStateFlow(null)
    val currentParentAccessory: StateFlow<Accessory?> = _currentParentAccessory

    fun updateName(name: String) {
        if (accessory.value != null) {
            _accessory.value = accessory.value!!.copy(
                name = name
            )
        }
    }

    fun updateDescription(description: String) {
        if (accessory.value != null) {
            _accessory.value = accessory.value!!.copy(
                description = description
            )
        }
    }

    fun updateAcquisitionDate(millis: Long) {
        if (accessory.value != null) {
            val buildDateInstant = Instant.fromEpochMilliseconds(millis)
            val buildDate =
                buildDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            _accessory.value = accessory.value!!.copy(
                acquisitionDate = buildDate
            )
        }
    }

    fun updateLastUsedDate(millis: Long?) {
        if (accessory.value != null) {
            if (millis != null) {
                val lastUsedDateInstant = Instant.fromEpochMilliseconds(millis)
                val lastUsedDate =
                    lastUsedDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                _accessory.value = accessory.value!!.copy(
                    lastUsedDate = lastUsedDate
                )
            } else {
                _accessory.value = accessory.value!!.copy(
                    lastUsedDate = null
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

    fun updateParentComponent(accessoryUid: Long?) {
        if (accessoryUid != null) {
            viewModelScope.launch {
                _currentParentAccessory.value = repository.getAccessoryByUid(accessoryUid)
            }
        } else {
            _currentParentAccessory.value = null
        }
    }

    fun commitAccessory() {
        if (accessory.value != null) {
            viewModelScope.launch {
                repository.updateAccessory(accessory.value!!)
            }
        }
    }

    init {
        if (accessoryUid > 0) {
            viewModelScope.launch {
                _accessory.value = repository.getAccessoryByUid(accessoryUid)
                if (accessory.value?.parentAccessoryUid != null) {
                    _currentParentAccessory.value =
                        repository.getAccessoryByUid(accessory.value!!.parentAccessoryUid!!)
                }
            }
        }
    }

    @AssistedFactory
    interface AccessoryEditViewModelFactory {
        fun create(componentUid: Long): AccessoryEditViewModel
    }
}