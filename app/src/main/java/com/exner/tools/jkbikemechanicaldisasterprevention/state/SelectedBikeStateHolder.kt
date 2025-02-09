package com.exner.tools.jkbikemechanicaldisasterprevention.state

import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import kotlinx.coroutines.flow.StateFlow

interface SelectedBikeStateHolder {
    val selectedBikeState: StateFlow<SelectedBikeState>
    fun updateSelectedBike(bike: Bike)
}