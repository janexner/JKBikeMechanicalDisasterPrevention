package com.exner.tools.kjsbikemaintenancechecker.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.exner.tools.kjsbikemaintenancechecker.R
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.TemplateActivity


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
    Text(text = "Created on ${activity?.createdDate}")
    DefaultSpacer()
    if (activity?.dueDate != null) {
        Text(text = "Due on ${activity.dueDate}")
    } else {
        Text(text = stringResource(R.string.no_due_date))
    }
    DefaultSpacer()
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