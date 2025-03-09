package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ComponentEditViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.RetirementReasonSelector
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.WearLevelSelector
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeEditDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.ManageComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Destination<RootGraph>
@Composable
fun ComponentEdit(
    componentUid: Long,
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_edit_a_component))

    val componentEditViewModel =
        hiltViewModel<ComponentEditViewModel, ComponentEditViewModel.ComponentEditViewModelFactory> { factory ->
            factory.create(componentUid = componentUid)
        }

    var modified by remember { mutableStateOf(false) }

    KJsResponsiveNavigation(
        BikeEditDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_text_cancel),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            ),
            KJsAction(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.btn_text_delete),
                onClick = {
                    destinationsNavigator.navigate(
                        ComponentDeleteDestination(componentUid)
                    )
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(R.string.btn_text_save),
            onClick = {
                componentEditViewModel.commitComponent()
                modified = false
                destinationsNavigator.popBackStack(
                    ManageComponentsDestination, inclusive = false
                )
            },
            enabled = modified
        ),
        headline = stringResource(R.string.hdr_edit_a_component)
    ) {
        val component by componentEditViewModel.component.observeAsState()

        val acquisitionDateInstant = component?.let {
            it.acquisitionDate?.let { it1 ->
                LocalDateTime(it1, LocalTime(12, 0, 0)).toInstant(
                    TimeZone.currentSystemDefault()
                )
            }
        }
        var selectedAcquisitionDate = acquisitionDateInstant?.toEpochMilliseconds()
        val firstUseDateInstant = component?.let {
            it.firstUseDate?.let { it1 ->
                LocalDateTime(it1, LocalTime(12, 0, 0)).toInstant(TimeZone.currentSystemDefault())
            }
        }
        var selectedFirstUseDate = firstUseDateInstant?.toEpochMilliseconds()
        val lastCheckDateInstant = component?.let {
            it.lastCheckDate?.let { it1 ->
                LocalDateTime(it1, LocalTime(12, 0, 0)).toInstant(TimeZone.currentSystemDefault())
            }
        }
        var selectedLastCheckDate = lastCheckDateInstant?.toEpochMilliseconds()
        val retirementDateInstant = component?.let {
            it.retirementDate?.let { it1 ->
                LocalDateTime(it1, LocalTime(12, 0, 0)).toInstant(TimeZone.currentSystemDefault())
            }
        }
        var selectedRetirementDate = retirementDateInstant?.toEpochMilliseconds()

        val bikes: List<Bike> by componentEditViewModel.observeBikes.collectAsStateWithLifecycle(
            emptyList()
        )
        val currentBike: Bike? by componentEditViewModel.currentBike.collectAsStateWithLifecycle(
            initialValue = null
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(0.7f)
            ) {
                DefaultTextFieldWithSpacer(
                    value = component?.name ?: stringResource(R.string.placehldr_name),
                    label = stringResource(R.string.lbl_component_name),
                    onValueChange = {
                        componentEditViewModel.updateName(it)
                        modified = true
                    }
                )
                DefaultTextFieldWithSpacer(
                    value = component?.description
                        ?: stringResource(R.string.placehldr_description),
                    label = stringResource(R.string.lbl_description),
                    onValueChange = {
                        componentEditViewModel.updateDescription(it)
                        modified = true
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedAcquisitionDate,
                    label = stringResource(R.string.lbl_acquisition_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedAcquisitionDate = it
                        componentEditViewModel.updateAcquisitionDate(it)
                        modified = true
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedFirstUseDate,
                    label = stringResource(R.string.lbl_first_use_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedFirstUseDate = it
                        componentEditViewModel.updateFirstUsedDate(it)
                        modified = true
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedLastCheckDate,
                    label = stringResource(R.string.lbl_last_checked_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedLastCheckDate = it
                        componentEditViewModel.updateLastCheckDate(it)
                        modified = true
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (component?.lastCheckMileage ?: "").toString(),
                    label = stringResource(R.string.lbl_last_checked_mileage),
                    onValueChange = {
                        componentEditViewModel.updateLastCheckMileage(it.toIntOrNull() ?: 0)
                        modified = true
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (component?.currentMileage ?: "").toString(),
                    label = stringResource(R.string.lbl_mileage),
                    onValueChange = {
                        componentEditViewModel.updateCurrentMileage(it.toIntOrNull() ?: 0)
                        modified = true
                    }
                )
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike != null) {
                        currentBike!!.name
                    } else {
                        stringResource(R.string.dropdown_item_none)
                    },
                    label = stringResource(R.string.lbl_attached_to_bike),
                    bikes = bikes,
                    onMenuItemClick = {
                        componentEditViewModel.updateBike(currentBike?.uid)
                        modified = true
                    }
                )
                DefaultTextFieldWithSpacer(
                    value = component?.titleForAutomaticActivities ?: "",
                    label = stringResource(R.string.lbl_title_for_automatic_activities),
                    onValueChange = {
                        componentEditViewModel.updateTitleForAutomaticActivities(it)
                        modified = true
                    }
                )
                WearLevelSelector(
                    currentWearLevel = component?.wearLevel,
                    onWearLevelSelected = {
                        componentEditViewModel.updateWearLevel(it)
                        modified = true
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedRetirementDate,
                    label = stringResource(R.string.lbl_retirement_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedRetirementDate = it
                        componentEditViewModel.updateRetirementDate(it)
                        modified = true
                    }
                )
                RetirementReasonSelector(
                    currentRetirementReason = component?.retirementReason,
                    onRetirementReasonSelected = {
                        componentEditViewModel.updateRetirementReason(it)
                        modified = true
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (component?.checkIntervalMiles ?: "").toString(),
                    label = stringResource(R.string.lbl_check_interval_miles),
                    onValueChange = {
                        componentEditViewModel.updateCheckIntervalMileage(it.toIntOrNull() ?: 0)
                        modified = true
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (component?.checkIntervalDays ?: "").toString(),
                    label = stringResource(R.string.lbl_check_interval_days),
                    onValueChange = {
                        componentEditViewModel.updateCheckIntervalDays(it.toIntOrNull() ?: 0)
                        modified = true
                    }
                )
            }
        }
    }
}