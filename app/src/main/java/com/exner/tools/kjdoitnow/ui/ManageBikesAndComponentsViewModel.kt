package com.exner.tools.kjdoitnow.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjdoitnow.database.KJsRepository
import com.exner.tools.kjdoitnow.ui.components.bikeAndComponentTreeToListOfString
import com.exner.tools.kjdoitnow.ui.components.createBikeAndComponentTree
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            val bikeAndComponentTree = createBikeAndComponentTree(repository)
            val treeAsStrings = bikeAndComponentTreeToListOfString(bikeAndComponentTree)
            if (treeAsStrings.isNotEmpty()) {
                treeAsStrings.forEach { line ->
                    Log.d("TEST", line)
                }
            }
        }
    }
}
