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
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.BikeEditViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.BikeEditDestination
import com.ramcosta.composedestinations.generated.destinations.ManageBikesDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Destination<RootGraph>
@Composable
fun BikeEdit(
    bikeUid: Long,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    val bikeEditViewModel =
        hiltViewModel<BikeEditViewModel, BikeEditViewModel.BikeEditViewModelFactory> { factory ->
            factory.create(bikeUid = bikeUid)
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
                        BikeDeleteDestination(bikeUid = bikeUid)
                    )
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(R.string.btn_text_save),
            onClick = {
                bikeEditViewModel.commitBike()
                modified = false
                destinationsNavigator.popBackStack(
                    ManageBikesDestination, inclusive = false
                )
            },
            enabled = modified
        ),
        headline = stringResource(R.string.hdr_edit_a_bike)
    ) {
        val bike by bikeEditViewModel.bike.observeAsState()
        val buildDateInstant = bike?.let {
            LocalDateTime(it.buildDate, LocalTime(12, 0, 0)).toInstant(
                TimeZone.currentSystemDefault()
            )
        }
        var selectedBuildDate = buildDateInstant?.toEpochMilliseconds()
        val lastUsedDateInstant = bike?.let {
            it.lastUsedDate?.let { it1 ->
                LocalDateTime(it1, LocalTime(12, 0, 0)).toInstant(
                    TimeZone.currentSystemDefault()
                )
            }
        }
        var selectedLastUsedDate = lastUsedDateInstant?.toEpochMilliseconds()

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
                    value = bike?.name ?: stringResource(R.string.placehldr_name),
                    onValueChange = {
                        bikeEditViewModel.updateName(it)
                        modified = true
                    },
                    label = stringResource(R.string.lbl_bike_name),
                )
                TextAndSwitch(
                    text = stringResource(R.string.lbl_is_an_ebike),
                    checked = bike?.isElectric ?: false
                ) {
                    bikeEditViewModel.updateIsElectric(it)
                    modified = true
                }
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedBuildDate,
                    label = stringResource(R.string.lbl_build_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedBuildDate = it
                        if (it != null) {
                            bikeEditViewModel.updateBuildDate(it)
                        }
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = (bike?.mileage ?: "").toString(),
                    onValueChange = { value ->
                        bikeEditViewModel.updateMileage(value.toIntOrNull() ?: 0)
                        modified = true
                    },
                    label = stringResource(R.string.lbl_mileage),
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedLastUsedDate,
                    label = stringResource(R.string.lbl_last_used_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedLastUsedDate = it
                        bikeEditViewModel.updateLastUsedDate(it)
                    }
                )
            }
        }
    }
}