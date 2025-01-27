package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ManageBikesAndComponentsViewModel @Inject constructor(
    userPreferencesManager: UserPreferencesManager,
    repository: KJsRepository
) : ViewModel() {

    val bikes = repository.observeBikes

    val components = repository.observeComponents

    private val _currentBike = MutableStateFlow(-1L)
    val currentBike: StateFlow<Long> = _currentBike

    fun updateCurrentBike(uid: Long) {
        _currentBike.value = uid
    }
}
