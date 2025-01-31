package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.ui.BikeAddViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TextAndSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ManageBikesAndComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Destination<RootGraph>
@Composable
fun BikeAdd(
    bikeAddViewModel: BikeAddViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    // input fields
    var name by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var mileage by remember { mutableIntStateOf(0) }
    var addComponentsWhenSavingBike by remember { mutableStateOf(false) }

    var modified by remember { mutableStateOf(false) }
    var created by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Text(text = "A new bike! Brilliant!")
                DefaultSpacer()
                DefaultTextFieldWithSpacer(
                    value = name,
                    onValueChange = {
                        name = it
                        modified = true
                    },
                    label = "Name",
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedDate,
                    label = "Build date",
                    placeholder = "DD/MM/YYYY",
                    onDateSelected = {
                        selectedDate = it
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = mileage.toString(),
                    onValueChange = {
                        mileage = it.toIntOrNull() ?: 0
                    },
                    label = "Mileage",
                )
                DefaultSpacer()
                TextAndSwitch(
                    text = "Add set of components",
                    checked = addComponentsWhenSavingBike
                ) {
                    addComponentsWhenSavingBike = it
                }
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
                },
                floatingActionButton = {
                    if (name.isNotBlank() && selectedDate != null) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Save") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Save the bike"
                                )
                            },
                            onClick = {
                                val bike = Bike(
                                    name = name,
                                    buildDate = Instant.fromEpochMilliseconds(selectedDate!!)
                                        .toLocalDateTime(
                                            TimeZone.currentSystemDefault()
                                        ).date,
                                    mileage = mileage,
                                    lastUsedDate = null,
                                    uid = 0 // autogenerate
                                )
                                bikeAddViewModel.saveNewBike(
                                    bike = bike,
                                    addComponents = addComponentsWhenSavingBike
                                )
                                modified = false
                                created = true
                                destinationsNavigator.popBackStack(
                                    ManageBikesAndComponentsDestination, inclusive = false
                                )
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

fun convertMillisToDate(millis: Long): String {
    return Instant.fromEpochMilliseconds(millis)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
}
