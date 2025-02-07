package com.exner.tools.jkbikemechanicaldisasterprevention.state

import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike

data class SelectedBikeState(
    val selectedBike: Bike? = null
)
