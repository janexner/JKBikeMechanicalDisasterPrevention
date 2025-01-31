package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.BikeEditViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentAddDestination
import com.ramcosta.composedestinations.generated.destinations.ManageBikesAndComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Destination<RootGraph>
@Composable
fun BikeEdit(
    bikeUid: Long,
    destinationsNavigator: DestinationsNavigator
) {
    val bikeEditViewModel =
        hiltViewModel<BikeEditViewModel, BikeEditViewModel.BikeEditViewModelFactory> { factory ->
            factory.create(bikeUid = bikeUid)
        }

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
                    value = bike?.name ?: "Name",
                    onValueChange = {
                        bikeEditViewModel.updateName(it)
                        modified = true
                    },
                    label = "Bike name",
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedBuildDate,
                    label = "Build date",
                    placeholder = "DD/MM/YYYY",
                    onDateSelected = {
                        selectedBuildDate = it
                        if (it != null) {
                            bikeEditViewModel.updateBuildDate(it)
                        }
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = bike?.mileage.toString(),
                    onValueChange = { value ->
                        bikeEditViewModel.updateMileage(value.toIntOrNull() ?: 0)
                        modified = true
                    },
                    label = "Mileage",
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedLastUsedDate,
                    label = "Last used date",
                    placeholder = "DD/MM/YYYY",
                    onDateSelected = {
                        selectedLastUsedDate = it
                        bikeEditViewModel.updateLastUsedDate(it)
                    }
                )

                DefaultSpacer()
                // TODO list components
                DefaultSpacer()
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        destinationsNavigator.navigate(ComponentAddDestination(bikeUid = bikeUid))
                    }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add component"
                        )
                        IconSpacer()
                        Text(text = "Add a component")
                    }
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

                    IconSpacer()
                    IconButton(onClick = {
                        destinationsNavigator.navigate(BikeDeleteDestination(bikeUid = bikeUid))
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
                                    contentDescription = "Save the bike"
                                )
                            },
                            onClick = {
                                bikeEditViewModel.commitBike()
                                modified = false
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