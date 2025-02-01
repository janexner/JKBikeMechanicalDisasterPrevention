package com.exner.tools.kjsbikemaintenancechecker.ui.components

import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component

data class BikeAndComponentTree(
    var bikes: List<BikeAndComponents>,
    var repository: KJsRepository,
) {

    init {
        val allBikes = emptyList<BikeAndComponents>()
        bikes.forEach { bike ->
            val allBike = BikeAndComponents(
                bike.bike,
                emptyList()
            )
        }
        
    }
}

data class BikeAndComponents(
    var bike: Bike,
    var components: List<Components>,
)

class Components(
    component: Component,
    attachedComponents: List<Components>,
)
