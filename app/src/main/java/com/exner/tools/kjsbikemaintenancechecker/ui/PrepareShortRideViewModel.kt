package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrepareShortRideViewModel @Inject constructor(
    val userPreferencesManager: UserPreferencesManager,
    private val repository: KJsRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val preferredBikeUid = userPreferencesManager.defaultBikeUidShort().lastOrNull()
            if (preferredBikeUid != null && preferredBikeUid > 0) {
                val preferredBike = repository.getBikeByUid(preferredBikeUid)
                if (preferredBike != null) {
                    _currentBike.value = preferredBike
                }
            }
        }
    }

    val observeBikesRaw = repository.observeBikes

    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike
    fun updateCurrentBike(bike: Bike?) {
        _currentBike.value = bike
        if (bike != null) {
            viewModelScope.launch {
                userPreferencesManager.setDefaultBikeUidShort(bike.uid)
            }
        }
    }

    val observeActivitiesByDueDate = repository.observeActivitiesByDueDate

    fun updateActivity(activity: Activity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
    }

    val observeActivitiesByBikes = repository.observeActivitiesByBikes

}