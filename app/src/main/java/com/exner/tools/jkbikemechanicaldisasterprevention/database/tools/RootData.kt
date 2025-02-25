package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RootData(
    val bikes: List<Bike>?,
    val activities: List<Activity>?,
    val templateActivities: List<TemplateActivity>?
)
