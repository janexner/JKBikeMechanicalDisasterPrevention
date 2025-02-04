package com.exner.tools.kjdoitnow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjdoitnow.database.KJsRepository
import com.exner.tools.kjdoitnow.database.entities.Activity
import com.exner.tools.kjdoitnow.database.entities.Bike
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityAddViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val observeBikes = repository.observeBikes
    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike

    fun updateAttachedBike(bikeUid: Long?) {
        if (bikeUid != null) {
            viewModelScope.launch {
                _currentBike.value = repository.getBikeByUid(bikeUid)
            }
        } else {
            _currentBike.value = null
        }
    }

    fun saveActivity(activity: Activity) {
        viewModelScope.launch {
            repository.insertActivity(activity)
        }
    }
}