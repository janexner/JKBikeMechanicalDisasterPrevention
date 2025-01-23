package com.exner.tools.kjsbikemaintenancechecker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivitiesByBikes
import com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination.invoke
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun TodoListItem(
    activity: ActivitiesByBikes,
    destinationsNavigator: DestinationsNavigator,
    onCheckboxCallback: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable {
                destinationsNavigator.navigate(
                    com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination(
                        activity.activityUid
                    )
                )
            },
    ) {
        ListItem(
            headlineContent = {
                val headline = if (activity.activityDueDate != null) {
                    "${activity.activityDueDate.toLocalDate()} - ${activity.activityTitle}"
                } else {
                    "before the ride - ${activity.activityTitle}"
                }
                Text(text = headline)
            },
            supportingContent = {
                Text(text = activity.activityDescription)
            },
            trailingContent = {
                Checkbox(
                    checked = activity.activityIsCompleted,
                    onCheckedChange = {
                        onCheckboxCallback(!activity.activityIsCompleted)
                    }
                )
            }
        )
    }
}