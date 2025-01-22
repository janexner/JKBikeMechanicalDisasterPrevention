package com.exner.tools.kjsbikemaintenancechecker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination.invoke
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun TodoListItem(
    activity: Activity,
    destinationsNavigator: DestinationsNavigator,
    onCheckboxCallback: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable {
                destinationsNavigator.navigate(
                    com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination(
                        activity.uid
                    )
                )
            },
    ) {
        ListItem(
            headlineContent = {
                Text(text = "${activity.dueDate.toLocalDate()} - ${activity.title}")
            },
            supportingContent = {
                Text(text = activity.description)
            },
            trailingContent = {
                Checkbox(
                    checked = activity.isCompleted,
                    onCheckedChange = {
                        onCheckboxCallback(!activity.isCompleted)
                    }
                )
            }
        )
    }
}