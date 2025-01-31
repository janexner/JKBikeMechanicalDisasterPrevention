package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.ui.ActivityAddViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TextAndSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Destination<RootGraph>
@Composable
fun ActivityAdd(
    activityAddViewModel: ActivityAddViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var selectedCreatedDate by remember {
        mutableLongStateOf(
            Clock.System.now().toEpochMilliseconds()
        )
    }
    var selectedDueDate by remember { mutableStateOf<Long?>(null) }

    val bikes: List<Bike> by activityAddViewModel.observeBikes.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentBike: Bike? by activityAddViewModel.currentBike.collectAsStateWithLifecycle(
        initialValue = null
    )

    var modified by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                DefaultTextFieldWithSpacer(
                    value = title,
                    onValueChange = {
                        title = it
                        modified = true
                    },
                    label = "Activity title",
                )
                DefaultTextFieldWithSpacer(
                    value = description,
                    onValueChange = {
                        description = it
                        modified = true
                    },
                    label = "Description",
                )
                TextAndSwitch(
                    text = "Completed",
                    checked = isCompleted
                ) {
                    isCompleted = it
                    modified = true
                }
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike != null) { currentBike!!.name } else { "None" },
                    label = "Attached to bike",
                    onMenuItemClick = {
                        activityAddViewModel.updateAttachedBike(it)
                    },
                    bikes = bikes
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedCreatedDate,
                    label = "Created date",
                    placeholder = "DD/MM/YYYY",
                    onDateSelected = {
                        if (it != null) {
                            selectedCreatedDate = it
                        }
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedDueDate,
                    label = "Due date",
                    placeholder = "DD/MM/YYYY",
                    onDateSelected = {
                        selectedDueDate = it
                    }
                )

            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        destinationsNavigator.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Cancel"
                        )
                    }

                    IconSpacer()
                    IconButton(onClick = {
                        // destinationsNavigator.navigate(ActivityDeleteDestination(activityUid = activityUid))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                },
                floatingActionButton = {
                    if (modified) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Save") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Save the activity"
                                )
                            },
                            onClick = {
                                val activity = Activity(
                                    title = title,
                                    description = description,
                                    createdDate = Instant.fromEpochMilliseconds(selectedCreatedDate)
                                        .toLocalDateTime(
                                            TimeZone.currentSystemDefault()
                                        ).date,
                                    isCompleted = isCompleted,
                                    dueDate = Instant.fromEpochMilliseconds(selectedDueDate!!)
                                        .toLocalDateTime(
                                            TimeZone.currentSystemDefault()
                                        ).date,
                                    bikeUid = if (currentBike != null) { currentBike!!.uid } else { 0 }
                                )
                                activityAddViewModel.saveActivity(activity)
                                modified = false
                                destinationsNavigator.navigateUp()
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                    }
                }
            )
        }
    )
}