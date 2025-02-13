package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Accessory
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RootData(
    val bikes: List<Bike>?,
    val components: List<Component>?,
    val accessories: List<Accessory>?,
    val templateActivities: List<TemplateActivity>?
)
