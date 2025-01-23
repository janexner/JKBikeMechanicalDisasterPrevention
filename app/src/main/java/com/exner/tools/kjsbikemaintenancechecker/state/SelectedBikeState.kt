package com.exner.tools.kjsbikemaintenancechecker.state

import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike

data class SelectedBikeState(
    val selectedBike: Bike? = null
)
