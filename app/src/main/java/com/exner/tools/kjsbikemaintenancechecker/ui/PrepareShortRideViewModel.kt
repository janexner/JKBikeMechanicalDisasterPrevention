package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import com.exner.tools.kjsbikemaintenancechecker.state.SelectedBikeStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ShortRideVM"

@HiltViewModel
class PrepareShortRideViewModel @Inject constructor(
    val userPreferencesManager: UserPreferencesManager,
    private val repository: KJsRepository
) : ViewModel() {

    val observeBikesRaw = repository.observeBikes

    fun updateActivity(activity: Activity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
    }

    val observeActivitiesByBikes = repository.observeActivitiesByBikes

}