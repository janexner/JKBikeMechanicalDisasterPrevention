package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrepareShortRideViewModel @Inject constructor(
    val userPreferencesManager: UserPreferencesManager,
    repository: KJsRepository
) : ViewModel() {

    val defaultBikeUidShort: StateFlow<Long> = userPreferencesManager.defaultBikeUidShort().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = -1L
    )
    fun updateDefaultBikeUidShort(newBikeUid: Long) {
        viewModelScope.launch {
            userPreferencesManager.setDefaultBikeUidShort(newBikeUid)
        }
    }

    val observeBikesRaw = repository.observeBikes

    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike
    fun updateCurrentBike(bike: Bike?) {
        _currentBike.value = bike
    }

}