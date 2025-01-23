package com.exner.tools.kjsbikemaintenancechecker.state

import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import kotlinx.coroutines.flow.StateFlow

interface SelectedBikeStateHolder {
    val selectedBikeState: StateFlow<SelectedBikeState>
    fun updateSelectedBike(bike: Bike)
}