package com.exner.tools.kjdoitnow.state

import com.exner.tools.kjdoitnow.database.entities.Bike

data class SelectedBikeState(
    val selectedBike: Bike? = null
)
