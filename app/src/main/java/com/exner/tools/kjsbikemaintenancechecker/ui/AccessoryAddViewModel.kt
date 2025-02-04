package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Accessory
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessoryAddViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val observeBikes = repository.observeBikes
    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike

    val observeAccessories = repository.observeAccessories
    private val _currentParentAccessory: MutableStateFlow<Accessory?> = MutableStateFlow(null)
    val currentParentAccessory: StateFlow<Accessory?> = _currentParentAccessory

    fun updateAttachedBike(bikeUid: Long?) {
        if (bikeUid != null) {
            viewModelScope.launch {
                _currentBike.value = repository.getBikeByUid(bikeUid)
            }
        } else {
            _currentBike.value = null
        }
    }

    fun updateParentAccessory(accessoryUid: Long?) {
        if (accessoryUid != null) {
            viewModelScope.launch {
                _currentParentAccessory.value = repository.getAccessoryByUid(accessoryUid)
            }
        } else {
            _currentParentAccessory.value = null
        }
    }

    fun saveNewAccessory(accessory: Accessory) {
        viewModelScope.launch {
            repository.insertAccessory(accessory)
        }
    }
}