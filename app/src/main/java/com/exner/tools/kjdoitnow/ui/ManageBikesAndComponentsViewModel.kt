package com.exner.tools.kjdoitnow.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.kjdoitnow.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ManageBikesAndComponentsViewModel @Inject constructor(
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
