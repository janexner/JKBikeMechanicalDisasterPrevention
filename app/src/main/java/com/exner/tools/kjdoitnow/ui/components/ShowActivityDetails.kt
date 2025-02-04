package com.exner.tools.kjdoitnow.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.exner.tools.kjdoitnow.R
import com.exner.tools.kjdoitnow.database.entities.Activity
import com.exner.tools.kjdoitnow.database.entities.TemplateActivity
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Composable
fun ShowActivityDetails(activity: Activity?) {
    Text(
        text = activity?.title ?: stringResource(R.string.no_activity_title),
        style = MaterialTheme.typography.headlineMedium
    )
    DefaultSpacer()
    Text(text = activity?.description ?: stringResource(R.string.placehldr_no_description))
    DefaultSpacer()
    Text(
        text = stringResource(R.string.activity_is) + if (activity?.isCompleted == true) {
            ""
        } else {
            stringResource(R.string.not)
        } + stringResource(R.string.completed)
    )
    DefaultSpacer()
    val createdLocalDateTime =
        activity?.createdInstant?.toLocalDateTime(TimeZone.currentSystemDefault())
    Text(text = "Created at $createdLocalDateTime")
    DefaultSpacer()
    if (activity?.dueDate != null) {
        Text(text = "Due on ${activity.dueDate}")
    } else {
        Text(text = stringResource(R.string.no_due_date))
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
    Text(text = "Ride Level: ${templateActivity?.rideLevel?.name}")
    DefaultSpacer()
    Text(
        text = templateActivity?.title ?: stringResource(R.string.no_activity_title),
        style = MaterialTheme.typography.headlineMedium
    )
    DefaultSpacer()
    Text(text = templateActivity?.description ?: stringResource(R.string.placehldr_no_description))
    DefaultSpacer()
}