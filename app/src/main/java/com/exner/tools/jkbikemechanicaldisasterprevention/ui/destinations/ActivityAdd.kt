package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultRideLevelSelectorActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityAddDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Destination<RootGraph>
@Composable
fun ActivityAdd(
    activityAddViewModel: ActivityAddViewModel = hiltViewModel(),
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_add_activity))

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
    var rideLevel: Int? by remember { mutableStateOf(null) }
    val currentBike: Bike? by activityAddViewModel.currentBike.collectAsStateWithLifecycle(
        initialValue = null
    )

    var modified by remember { mutableStateOf(false) }

    KJsResponsiveNavigation(
        ActivityAddDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.btn_text_cancel),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Save,
            contentDescription = stringResource(R.string.btn_text_save),
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
                    componentUid = null,
                    bikeUid = currentBike?.uid
                )
                activityAddViewModel.saveActivity(activity)
                modified = false
                destinationsNavigator.navigateUp()
            },
            enabled = modified && title.isNotBlank()
        ),
        headline = stringResource(R.string.hdr_add_activity)
    ) {
        val bikes: List<Bike> by activityAddViewModel.observeBikes.collectAsStateWithLifecycle(
            emptyList()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
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
                DefaultRideLevelSelectorActivity(
                    rideLevel = rideLevel
                ) {
                    rideLevel = it
                    modified = true
                }
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
                        stringResource(R.string.dropdown_item_none)
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
        }
    }
}