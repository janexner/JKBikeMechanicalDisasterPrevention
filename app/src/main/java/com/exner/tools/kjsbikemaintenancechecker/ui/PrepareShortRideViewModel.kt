package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrepareShortRideViewModel @Inject constructor(
    val userPreferencesManager: UserPreferencesManager,
    private val repository: KJsRepository
) : ViewModel() {

    val observeBikesRaw = repository.observeBikes

    val observeActivitiesByBikes = repository.observeActivityWithBikeDataAndDueDateOrderedByDueDate

    fun updateActivity(activity: Activity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
    }

}