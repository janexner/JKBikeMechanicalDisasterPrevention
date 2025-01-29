package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.ui.BikeAddViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DatePickerModal
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TextAndSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
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

    val mileageFieldChange: (String) -> Unit = { it ->
        mileage = it.toIntOrNull() ?: 0
    }

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
                var showModal by remember { mutableStateOf(false) }

                Text(text = "A new bike! Brilliant!")
                DefaultSpacer()
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        modified = true
                    },
                    label = { Text(text = "Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DefaultSpacer()
                OutlinedTextField(
                    value = selectedDate?.let { convertMillisToDate(it) } ?: "",
                    onValueChange = { },
                    label = { Text("Build date") },
                    placeholder = { Text("DD/MM/YYYY") },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(selectedDate) {
                            awaitEachGesture {
                                // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                                // in the Initial pass to observe events before the text field consumes them
                                // in the Main pass.
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val upEvent =
                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (upEvent != null) {
                                    showModal = true
                                }
                            }
                        }
                )

                if (showModal) {
                    DatePickerModal(
                        onDateSelected = {
                            selectedDate = it
                        },
                        onDismiss = { showModal = false }
                    )
                }
                DefaultSpacer()
                OutlinedTextField(
                    value = mileage.toString(),
                    onValueChange = mileageFieldChange,
                    label = { Text(text = "Mileage") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
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
                                    buildDate = Instant.fromEpochMilliseconds(selectedDate!!).toLocalDateTime(
                                        TimeZone.currentSystemDefault()).date,
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
    return Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
}
