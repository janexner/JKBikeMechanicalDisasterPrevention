package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.BikeEditViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DatePickerModal
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
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
    val bikeEditViewModel = hiltViewModel<BikeEditViewModel, BikeEditViewModel.BikeEditViewModelFactory> { factory ->
        factory.create(bikeUid = bikeUid)
    }

    val bike by bikeEditViewModel.bike.observeAsState()
    val buildDateInstant = bike?.let { LocalDateTime(it.buildDate, LocalTime(12, 0, 0)).toInstant(
        TimeZone.currentSystemDefault()) }
    var selectedBuildDate = buildDateInstant?.toEpochMilliseconds()
    var showBuildDateModal by remember { mutableStateOf(false) }
    val lastUsedDateInstant = bike?.let { it.lastUsedDate?.let { it1 -> LocalDateTime(it1, LocalTime(12, 0, 0) ).toInstant(
        TimeZone.currentSystemDefault()) } }
    var selectedLastUsedDate = lastUsedDateInstant?.toEpochMilliseconds()
    var showLastUsedDateModal by remember { mutableStateOf(false) }

    var modified by remember { mutableStateOf(false) }

    Scaffold (
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).padding(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = bike?.name ?: "Name",
                        onValueChange = {
                            bikeEditViewModel.updateName(it)
                            modified = true
                        },
                        label = { Text(text = "Bike name") },
                        singleLine = true,
                        modifier = Modifier.weight(0.75f)
                    )
                }
                DefaultSpacer()
                OutlinedTextField(
                    value = selectedBuildDate?.let { convertMillisToDate(it) } ?: "",
                    onValueChange = { },
                    label = { Text("Build date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(selectedBuildDate) {
                            awaitEachGesture {
                                // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                                // in the Initial pass to observe events before the text field consumes them
                                // in the Main pass.
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val upEvent =
                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (upEvent != null) {
                                    showBuildDateModal = true
                                }
                            }
                        }
                )

                if (showBuildDateModal) {
                    DatePickerModal(
                        onDateSelected = {
                            selectedBuildDate = it
                            if (it != null) {
                                bikeEditViewModel.updateBuildDate(it)
                            }
                        },
                        onDismiss = { showBuildDateModal = false }
                    )
                }
                DefaultSpacer()
                OutlinedTextField(
                    value = bike?.mileage.toString(),
                    onValueChange = { value ->
                        bikeEditViewModel.updateMileage(value.toIntOrNull() ?: 0)
                        modified = true
                    },
                    label = { Text(text = "Mileage") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DefaultSpacer()
                OutlinedTextField(
                    value = selectedLastUsedDate?.let { convertMillisToDate(it) } ?: "",
                    onValueChange = { },
                    label = { Text("Last used date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(selectedLastUsedDate) {
                            awaitEachGesture {
                                // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                                // in the Initial pass to observe events before the text field consumes them
                                // in the Main pass.
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val upEvent =
                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (upEvent != null) {
                                    showLastUsedDateModal = true
                                }
                            }
                        }
                )

                if (showLastUsedDateModal) {
                    DatePickerModal(
                        onDateSelected = {
                            selectedLastUsedDate = it
                            if (it != null) {
                                bikeEditViewModel.updateLastUsedDate(it)
                            }
                        },
                        onDismiss = { showLastUsedDateModal = false }
                    )
                }
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
                        Text(text = "Add component")
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
                                    ManageBikesAndComponentsDestination, inclusive = false)
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