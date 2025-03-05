package com.exner.tools.jkbikemechanicaldisasterprevention.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.RideLevel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Composable
fun ShowActivityDetails(activity: Activity?) {
    Text(
        text = activity?.title ?: stringResource(R.string.placehldr_no_activity_title),
        style = MaterialTheme.typography.headlineMedium
    )
    DefaultSpacer()
    Text(text = activity?.description ?: stringResource(R.string.placehldr_no_description))
    DefaultSpacer()
    Text(
        text = if (activity?.isCompleted == true) {
            stringResource(R.string.activity_is_completed)
        } else {
            stringResource(R.string.activity_is_not_completed)
        }
    )
    DefaultSpacer()
    val createdLocalDateTime =
        activity?.createdInstant?.toLocalDateTime(TimeZone.currentSystemDefault())
    Text(text = "Created at $createdLocalDateTime")
    DefaultSpacer()
    if (activity?.dueDate != null) {
        Text(text = "Due on ${activity.dueDate}")
    } else {
        Text(text = stringResource(R.string.placehldr_no_due_date))
    }
    if (activity?.isEBikeSpecific == true) {
        DefaultSpacer()
        Text(text = "It is eBike-specific.")
    }
    DefaultSpacer()
    if (activity != null) {
        if (activity.isCompleted && activity.doneInstant != null) {
            val doneLocalDateTime =
                activity.doneInstant.toLocalDateTime(TimeZone.currentSystemDefault())
            Text(text = "Completed at $doneLocalDateTime")
        }
    }
}

@Composable
fun ShowTemplateActivityDetails(
    templateActivity: TemplateActivity?
) {
    val context = LocalContext.current
    val levelLabel = if (templateActivity?.rideLevel != null) {
        buildString {
            append(stringResource(R.string.lbl_ride_level_name))
            append(RideLevel.getLabel(context, templateActivity.rideLevel))
        }
    } else {
        buildString {
            append(stringResource(R.string.lbl_ride_level_name))
            append(stringResource(R.string.placehldr_item_all_levels))
        }
    }
    Text(text = levelLabel)
    DefaultSpacer()
    Text(
        text = templateActivity?.title ?: stringResource(R.string.placehldr_no_activity_title),
        style = MaterialTheme.typography.headlineMedium
    )
    DefaultSpacer()
    Text(text = templateActivity?.description ?: stringResource(R.string.placehldr_no_description))
    DefaultSpacer()
}