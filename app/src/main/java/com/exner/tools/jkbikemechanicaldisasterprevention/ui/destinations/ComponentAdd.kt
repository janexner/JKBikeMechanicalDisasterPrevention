package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.RetirementReason
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ComponentAddViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.BikeSelector
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.RetirementReasonSelector
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.WearLevelSelector
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeAddDestination
import com.ramcosta.composedestinations.generated.destinations.ManageComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Destination<RootGraph>
@Composable
fun ComponentAdd(
    componentAddViewModel: ComponentAddViewModel = hiltViewModel(),
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_add_component))

    // input fields
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var acquisitionDate by remember { mutableStateOf<Long?>(null) }
    var firstUseDate by remember { mutableStateOf<Long?>(null) }
    var lastCheckDate by remember { mutableStateOf<Long?>(null) }
    var bike: Bike? by remember { mutableStateOf(null) }
    var checkIntervalMiles: Int? by remember { mutableStateOf(null) }
    var checkIntervalDays: Int? by remember { mutableStateOf(null) }
    var lastCheckMileage: Int? by remember { mutableStateOf(null) }
    var currentMileage: Int? by remember { mutableStateOf(null) }
    var titleForAutomaticActivities: String? by remember { mutableStateOf(null) }
    var wearLevel: WearLevel? by remember { mutableStateOf(null) }
    var retirementDate by remember { mutableStateOf<Long?>(null) }
    var retirementReason: RetirementReason? by remember { mutableStateOf(null) }

    val allBikes by componentAddViewModel.allBikes.collectAsStateWithLifecycle(emptyList())

    var modified by remember { mutableStateOf(false) }
    var created by remember { mutableStateOf(false) }

    KJsResponsiveNavigation(
        BikeAddDestination,
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
                val component = Component(
                    name = name,
                    description = description,
                    acquisitionDate = if (acquisitionDate != null) {
                        Instant.fromEpochMilliseconds(acquisitionDate!!)
                            .toLocalDateTime(
                                TimeZone.currentSystemDefault()
                            ).date
                    } else {
                        null
                    },
                    firstUseDate = if (firstUseDate != null) {
                        Instant.fromEpochMilliseconds(firstUseDate!!)
                            .toLocalDateTime(
                                TimeZone.currentSystemDefault()
                            ).date
                    } else {
                        null
                    },
                    lastCheckDate = if (lastCheckDate != null) {
                        Instant.fromEpochMilliseconds(lastCheckDate!!)
                            .toLocalDateTime(
                                TimeZone.currentSystemDefault()
                            ).date
                    } else {
                        null
                    },
                    bikeUid = bike?.uid,
                    checkIntervalMiles = checkIntervalMiles,
                    checkIntervalDays = checkIntervalDays,
                    lastCheckMileage = lastCheckMileage,
                    currentMileage = currentMileage,
                    titleForAutomaticActivities = titleForAutomaticActivities,
                    wearLevel = wearLevel,
                    retirementDate = if (retirementDate != null) {
                        Instant.fromEpochMilliseconds(retirementDate!!)
                            .toLocalDateTime(
                                TimeZone.currentSystemDefault()
                            ).date
                    } else {
                        null
                    },
                    retirementReason = retirementReason,
                    uid = 0L
                )
                componentAddViewModel.saveNewComponent(
                    component = component,
                )
                modified = false
                created = true
                destinationsNavigator.navigateUp()
                destinationsNavigator.popBackStack(
                    ManageComponentsDestination, inclusive = false
                )
            },
            enabled = modified && name.isNotBlank() && acquisitionDate != null
        ),
        headline = stringResource(R.string.hdr_add_component)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
                Text(text = stringResource(R.string.a_new_component_brilliant))
                DefaultSpacer()
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(0.7f)
            ) {
                DefaultTextFieldWithSpacer(
                    value = name,
                    onValueChange = {
                        name = it
                        modified = true
                    },
                    label = stringResource(R.string.lbl_name),
                )
                DefaultTextFieldWithSpacer(
                    value = description,
                    onValueChange = {
                        description = it
                        modified = true
                    },
                    label = stringResource(R.string.lbl_description),
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = acquisitionDate,
                    label = stringResource(R.string.lbl_acquisition_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        acquisitionDate = it
                        modified = true
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = firstUseDate,
                    label = stringResource(R.string.lbl_first_use_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        firstUseDate = it
                        modified = true
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = lastCheckDate,
                    label = stringResource(R.string.lbl_last_checked_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        lastCheckDate = it
                        modified = true
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (lastCheckMileage ?: "").toString(),
                    label = stringResource(R.string.lbl_last_checked_mileage),
                    placeholder = "0",
                    onValueChange = {
                        lastCheckMileage = it.toIntOrNull() ?: lastCheckMileage
                        modified = true
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (checkIntervalMiles ?: "").toString(),
                    label = stringResource(R.string.lbl_check_interval_miles),
                    placeholder = "0",
                    onValueChange = {
                        checkIntervalMiles = it.toIntOrNull() ?: checkIntervalMiles
                        modified = true
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (checkIntervalDays ?: "").toString(),
                    label = stringResource(R.string.lbl_check_interval_days),
                    placeholder = "0",
                    onValueChange = {
                        checkIntervalDays = it.toIntOrNull() ?: checkIntervalDays
                        modified = true
                    }
                )
                DefaultTextFieldWithSpacer(
                    value = titleForAutomaticActivities ?: "",
                    label = stringResource(R.string.lbl_title_for_automatic_activities),
                    onValueChange = {
                        titleForAutomaticActivities = it
                        modified = true
                    }
                )
                // bike selector here
                BikeSelector(
                    currentBike = bike,
                    bikes = allBikes
                ) { selectedBike ->
                    bike = selectedBike
                    modified = true
                }
                DefaultSpacer()
                DefaultNumberFieldWithSpacer(
                    value = (currentMileage ?: "").toString(),
                    label = stringResource(R.string.lbl_current_mileage),
                    placeholder = "0",
                    onValueChange = {
                        currentMileage = it.toIntOrNull() ?: currentMileage
                        modified = true
                    }
                )
                // wear level selector here
                WearLevelSelector(
                    currentWearLevel = wearLevel,
                    onWearLevelSelected = {
                        wearLevel = it
                        modified = true
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = retirementDate,
                    label = stringResource(R.string.lbl_retirement_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        retirementDate = it
                    }
                )
                RetirementReasonSelector(
                    currentRetirementReason = retirementReason,
                    onRetirementReasonSelected = {
                        retirementReason = it
                        modified = true
                    }
                )
            }
        }
    }
}

