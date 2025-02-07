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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ActivityEditViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultInstantSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityDeleteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Destination<RootGraph>
@Composable
fun ActivityEdit(
    activityUid: Long,
    destinationsNavigator: DestinationsNavigator,
) {

    val activityEditViewModel =
        hiltViewModel<ActivityEditViewModel, ActivityEditViewModel.ActivityEditViewModelFactory> { factory ->
            factory.create(activityUid = activityUid)
        }

    val activity by activityEditViewModel.activity.observeAsState()
    var selectedCreatedDate = activity?.createdInstant?.toEpochMilliseconds()
    val dueDateInstant = activity?.dueDate?.let {
        LocalDateTime(
            it,
            LocalTime(12, 0, 0)
        ).toInstant(TimeZone.currentSystemDefault())
    }
    var selectedDueDate = dueDateInstant?.toEpochMilliseconds()

    val bikes: List<Bike> by activityEditViewModel.observeBikes.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentBike: Bike? by activityEditViewModel.currentBike.collectAsStateWithLifecycle(
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
                PageHeaderTextWithSpacer(stringResource(R.string.edit_an_activity))
                DefaultTextFieldWithSpacer(
                    value = activity?.title ?: "",
                    label = stringResource(R.string.lbl_activity_title),
                    placeholder = stringResource(R.string.title),
                    onValueChange = {
                        activityEditViewModel.updateTitle(it)
                        modified = true
                    },
                )
                DefaultTextFieldWithSpacer(
                    value = activity?.description ?: "",
                    onValueChange = {
                        activityEditViewModel.updateDescription(it)
                        modified = true
                    },
                    label = stringResource(R.string.lbl_description),
                )
                TextAndSwitch(
                    text = stringResource(R.string.is_ebike_specific),
                    checked = activity?.isEBikeSpecific ?: false
                ) {
                    activityEditViewModel.updateIsEBikeSpecific(it)
                    modified = true
                }
                TextAndSwitch(
                    text = stringResource(R.string.lbl_completed),
                    checked = activity?.isCompleted ?: false
                ) {
                    activityEditViewModel.updateIsCompleted(it)
                    modified = true
                }
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike != null) { currentBike!!.name } else {
                        stringResource(R.string.none) },
                    label = stringResource(R.string.lbl_attached_to_bike),
                    onMenuItemClick = {
                        activityEditViewModel.updateAttachedBike(it)
                    },
                    bikes = bikes
                )
                DefaultInstantSelectorWithSpacer(
                    selectedDate = selectedCreatedDate,
                    label = stringResource(R.string.lbl_created_at),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd_hh_mm_ss),
                    onDateSelected = {
                        selectedCreatedDate = it
                        if (it != null) {
                            activityEditViewModel.updateCreatedDate(it)
                        }
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedDueDate,
                    label = stringResource(R.string.lbl_due_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedDueDate = it
                        activityEditViewModel.updateDueDate(it)
                    }
                )
                DefaultSpacer()

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
                         destinationsNavigator.navigate(ActivityDeleteDestination(activityUid = activityUid))
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
                                activityEditViewModel.commitActivity()
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