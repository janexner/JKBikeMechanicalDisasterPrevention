package com.exner.tools.kjdoitnow.state

import com.exner.tools.kjdoitnow.database.entities.Bike
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SelectedBikeStateHolderImpl @Inject constructor() : SelectedBikeStateHolder {
    private val _selectedBikeState = MutableStateFlow(SelectedBikeState())
    override val selectedBikeState: StateFlow<SelectedBikeState> = _selectedBikeState

    override fun updateSelectedBike(bike: Bike) {
        // atomic
        _selectedBikeState.update { current ->
            current.copy(selectedBike = bike)
        }
    }


}