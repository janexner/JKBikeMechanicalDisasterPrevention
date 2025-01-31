package com.exner.tools.kjsbikemaintenancechecker.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity


@Composable
fun ShowActivityDetails(activity: Activity?) {
    Text(
        text = activity?.title ?: "No activity title",
        style = MaterialTheme.typography.headlineMedium
    )
    DefaultSpacer()
    Text(text = activity?.description ?: "No description")
    DefaultSpacer()
    Text(
        text = "Activity is " + if (activity?.isCompleted == true) {
            ""
        } else {
            "not "
        } + "completed."
    )
    DefaultSpacer()
    Text(text = "Created on ${activity?.createdDate}")
    DefaultSpacer()
    if (activity?.dueDate != null) {
        Text(text = "Due on ${activity.dueDate}")
    } else {
        Text(text = "No due date")
    }
    DefaultSpacer()
}
