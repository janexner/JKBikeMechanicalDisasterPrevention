package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ActivityAddViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
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
    var isEBikeSpecific by remember { mutableStateOf(false) }

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
                PageHeaderTextWithSpacer(stringResource(R.string.add_activity))
                DefaultTextFieldWithSpacer(
                    value = title,
                    onValueChange = {
                        title = it
                        modified = true
                    },
                    label = stringResource(R.string.lbl_activity_title),
                )
                DefaultTextFieldWithSpacer(
                    value = description,
                    onValueChange = {
                        description = it
                        modified = true
                    },
                    label = stringResource(R.string.lbl_description),
                )
                TextAndSwitch(
                    text = stringResource(R.string.is_ebike_specific),
                    checked = isEBikeSpecific
                ) {
                    isEBikeSpecific = it
                    modified = true
                }
                TextAndSwitch(
                    text = stringResource(R.string.lbl_completed),
                    checked = isCompleted
                ) {
                    isCompleted = it
                    modified = true
                }
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike != null) {
                        currentBike!!.name
                    } else {
                        stringResource(R.string.none)
                    },
                    label = stringResource(R.string.lbl_attached_to_bike),
                    onMenuItemClick = {
                        activityAddViewModel.updateAttachedBike(it)
                    },
                    bikes = bikes
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedCreatedDate,
                    label = stringResource(R.string.lbl_created_at),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        if (it != null) {
                            selectedCreatedDate = it
                        }
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedDueDate,
                    label = stringResource(R.string.lbl_due_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
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
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }

                    IconSpacer()
                    IconButton(onClick = {
                        // destinationsNavigator.navigate(ActivityDeleteDestination(activityUid = activityUid))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
                floatingActionButton = {
                    if (modified) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = stringResource(R.string.save)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = stringResource(R.string.save_the_activity)
                                )
                            },
                            onClick = {
                                val activity = Activity(
                                    title = title,
                                    description = description,
                                    rideUid = null,
                                    isEBikeSpecific = isEBikeSpecific,
                                    rideLevel = null, // TODO
                                    createdInstant = Instant.fromEpochMilliseconds(
                                        selectedCreatedDate
                                    ),
                                    isCompleted = isCompleted,
                                    dueDate = if (selectedDueDate != null) {
                                        Instant.fromEpochMilliseconds(selectedDueDate!!)
                                            .toLocalDateTime(
                                                TimeZone.currentSystemDefault()
                                            ).date
                                    } else {
                                        null
                                    },
                                    doneInstant = null,
                                    bikeUid = currentBike?.uid
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