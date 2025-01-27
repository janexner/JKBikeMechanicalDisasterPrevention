package com.exner.tools.kjsbikemaintenancechecker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivitiesByBikes
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun TodoListItem(
    activity: ActivitiesByBikes,
    destinationsNavigator: DestinationsNavigator,
    onCheckboxCallback: (Boolean) -> Unit,
    suppressBikeBadge: Boolean = false,
    suppressDueDate: Boolean = false
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
            overlineContent = {
                if (activity.bikeName != null && !suppressBikeBadge) {
                    Text(text = activity.bikeName, color = MaterialTheme.colorScheme.tertiary)
                }
            },
            headlineContent = {
                val headline = if (activity.activityDueDate != null && !suppressDueDate) {
                    "${activity.activityDueDate.toLocalDate()} - ${activity.activityTitle}"
                } else {
                    activity.activityTitle
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