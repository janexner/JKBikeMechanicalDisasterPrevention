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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.BikeAddViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeAddDestination
import com.ramcosta.composedestinations.generated.destinations.ManageBikesDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Destination<RootGraph>
@Composable
fun BikeAdd(
    bikeAddViewModel: BikeAddViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    // input fields
    var name by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var mileage by remember { mutableIntStateOf(0) }
    var isElectric by remember { mutableStateOf(false) }

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
                val bike = Bike(
                    name = name,
                    buildDate = Instant.fromEpochMilliseconds(selectedDate!!)
                        .toLocalDateTime(
                            TimeZone.currentSystemDefault()
                        ).date,
                    mileage = mileage,
                    lastUsedDate = null,
                    isElectric = isElectric,
                    uid = 0 // autogenerate
                )
                bikeAddViewModel.saveNewBike(
                    bike = bike,
                )
                modified = false
                created = true
                destinationsNavigator.navigateUp()
                destinationsNavigator.popBackStack(
                    ManageBikesDestination, inclusive = false
                )
            },
            enabled = modified && name.isNotBlank() && selectedDate != null
        ),
        headline = stringResource(R.string.hdr_add_bike)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
                Text(text = stringResource(R.string.a_new_bike_brilliant))
                DefaultSpacer()
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                DefaultTextFieldWithSpacer(
                    value = name,
                    onValueChange = {
                        name = it
                        modified = true
                    },
                    label = stringResource(R.string.lbl_name),
                )
                TextAndSwitch(
                    text = stringResource(R.string.lbl_is_an_ebike),
                    checked = isElectric
                ) {
                    isElectric = it
                }
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedDate,
                    label = stringResource(R.string.lbl_build_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedDate = it
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = mileage.toString(),
                    onValueChange = {
                        mileage = it.toIntOrNull() ?: 0
                    },
                    label = stringResource(R.string.lbl_mileage),
                )
            }
        }
    }
}

