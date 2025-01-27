package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.AddBikeViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DatePickerModal
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun AddBike(
    addBikeViewModel: AddBikeViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val name by addBikeViewModel.name.observeAsState()
    val buildDate by addBikeViewModel.buildDate.observeAsState()
    val mileage by addBikeViewModel.mileage.observeAsState()
    val lastUsedDate by addBikeViewModel.lastUsedDate.observeAsState()

    var modified by remember { mutableStateOf(false) }

    Scaffold (
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).padding(8.dp)
            ) {
                var showModal by remember { mutableStateOf(false) }
                var selectedDate by remember { mutableStateOf<Long?>(null) }

                Text(text = "A new bike! Brilliant!")
                DefaultSpacer()
                OutlinedTextField(
                    value = name ?: "Name",
                    onValueChange = {
                        addBikeViewModel.updateName(it)
                        modified = true
                    },
                    label = { Text(text = "Name") },
                    singleLine = true,
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
                                val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (upEvent != null) {
                                    showModal = true
                                }
                            }
                        }
                )

                if (showModal) {
                    DatePickerModal(
                        onDateSelected = { selectedDate = it },
                        onDismiss = { showModal = false }
                    )
                }
                DefaultSpacer()
                OutlinedTextField(
                    value = mileage.toString() ?: "Mileage so far",
                    onValueChange = { input ->
                        // this is sketchy, so TODO
                        if (input.all { it.isDigit() }) {
                            addBikeViewModel.updateMileage(input.toInt())
                            modified = true
                        }
                    },
                    label = { Text(text = "Mileage") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                )
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
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Save") },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Save the bike"
                            )
                        },
                        onClick = {
                            // addComponentViewModel.saveComponent()
                            destinationsNavigator.navigateUp()
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            )
        }
    )
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}