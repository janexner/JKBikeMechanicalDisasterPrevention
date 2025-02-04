package com.exner.tools.kjdoitnow.state

import com.exner.tools.kjdoitnow.database.entities.Bike
import kotlinx.coroutines.flow.StateFlow

interface SelectedBikeStateHolder {
    val selectedBikeState: StateFlow<SelectedBikeState>
    fun updateSelectedBike(bike: Bike)
}