package com.exner.tools.kjsbikemaintenancechecker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.exner.tools.kjsbikemaintenancechecker.database.entities.TemplateActivity
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivityWithBikeData
import com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun TodoListItem(
    activity: ActivityWithBikeData,
    destinationsNavigator: DestinationsNavigator,
    onCheckboxCallback: (Boolean) -> Unit,
    suppressBikeBadge: Boolean = false,
    suppressDueDate: Boolean = false
) {
    Surface(
        modifier = Modifier
            .clickable {
                destinationsNavigator.navigate(
                    ActivityDetailsDestination(
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
                    "${activity.activityDueDate} - ${activity.activityTitle}"
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

@Composable
fun TransientTodoListItem(
    activity: ActivityWithBikeData,
    onCheckboxCallback: (Boolean) -> Unit,
    suppressDueDate: Boolean = false
) {
    ListItem(
        headlineContent = {
            val headline = if (activity.activityDueDate != null && !suppressDueDate) {
                "${activity.activityDueDate} - ${activity.activityTitle}"
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

@Composable
fun TemplateActivityListItem(
    templateActivity: TemplateActivity,
    onItemClick: (Long) -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onItemClick(templateActivity.uid) },
        overlineContent = {
            if (templateActivity.isEBikeSpecific) {
                Text(text = "eBike-specific")
            }
        },
        headlineContent = {
            Text(text = templateActivity.title)
        },
        supportingContent = {
            Text(text = templateActivity.description)
        },
    )
}
