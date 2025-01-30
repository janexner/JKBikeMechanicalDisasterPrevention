package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.ui.ComponentAddViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DatePickerModal
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun ComponentAdd(
    componentAddViewModel: ComponentAddViewModel = hiltViewModel(),
    bikeUid: Long?,
    destinationsNavigator: DestinationsNavigator
) {

    // input fields
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attachedBikeUid by remember { mutableStateOf(bikeUid) }
    var parentComponentUid by remember { mutableStateOf<Long?>(null) }
    var selectedAcquisitionDate by remember { mutableStateOf<Long?>(null) }
    var mileage by remember { mutableIntStateOf(0) }
    var selectedLastUsedDate by remember { mutableStateOf<Long?>(null) }

    var showBuildDateModal by remember { mutableStateOf(false) }
    var showLastUsedDateModal by remember { mutableStateOf(false) }

    val bikes: List<Bike> by componentAddViewModel.observeBikes.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentBike: Bike? by componentAddViewModel.currentBike.collectAsStateWithLifecycle(
        initialValue = null
    )

    val components: List<Component> by componentAddViewModel.observeComponents.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentParentComponent: Component? by componentAddViewModel.currentParentComponent.collectAsStateWithLifecycle(
        initialValue = null
    )

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
                Text(text = "A new component!")
                DefaultSpacer()
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = name,
                    onValueChange = {
                        name = it
                        modified = true
                    },
                    label = { Text(text = "Component name") },
                    singleLine = true,
                )
                DefaultSpacer()
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = description,
                    onValueChange = {
                        description = it
                        modified = true
                    },
                    label = { Text(text = "Description") },
                    singleLine = true,
                )
                DefaultSpacer()
                var bikeSelectorExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = bikeSelectorExpanded,
                    onExpandedChange = { bikeSelectorExpanded = !bikeSelectorExpanded }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            // The `menuAnchor` modifier must be passed to the text field for correctness.
                            modifier = Modifier
                                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth()
                                .padding(8.dp),
                            readOnly = true,
                            value = if (currentBike == null) "None" else currentBike!!.name,
                            onValueChange = {},
                            label = { Text("Attached to bike") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bikeSelectorExpanded) },
                        )
                    }
                    ExposedDropdownMenu(
                        expanded = bikeSelectorExpanded,
                        onDismissRequest = { bikeSelectorExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = "None") },
                            onClick = {
                                componentAddViewModel.updateAttachedBike(null)
                                attachedBikeUid = null
                                modified = true
                                bikeSelectorExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                        bikes.forEach { bike ->
                            DropdownMenuItem(
                                text = { Text(text = bike.name) },
                                onClick = {
                                    componentAddViewModel.updateAttachedBike(bike.uid)
                                    attachedBikeUid = bike.uid
                                    modified = true
                                    bikeSelectorExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                DefaultSpacer()
                var parentComponentExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = parentComponentExpanded,
                    onExpandedChange = { parentComponentExpanded = !parentComponentExpanded }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            // The `menuAnchor` modifier must be passed to the text field for correctness.
                            modifier = Modifier
                                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth()
                                .padding(8.dp),
                            readOnly = true,
                            value = if (currentParentComponent == null) "None" else currentParentComponent!!.name,
                            onValueChange = {},
                            label = { Text("Part of component") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = parentComponentExpanded) },
                        )
                    }
                    ExposedDropdownMenu(
                        expanded = parentComponentExpanded,
                        onDismissRequest = { parentComponentExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = "None") },
                            onClick = {
                                componentAddViewModel.updateParentComponent(null)
                                parentComponentUid = null
                                modified = true
                                parentComponentExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                        components.forEach { component ->
                            DropdownMenuItem(
                                text = { Text(text = component.name) },
                                onClick = {
                                    componentAddViewModel.updateParentComponent(component.uid)
                                    parentComponentUid = component.uid
                                    modified = true
                                    parentComponentExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                DefaultSpacer()
                OutlinedTextField(
                    value = selectedAcquisitionDate?.let { convertMillisToDate(it) } ?: "",
                    onValueChange = { },
                    label = { Text("Acquisition date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(selectedAcquisitionDate) {
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
                            selectedAcquisitionDate = it
                        },
                        onDismiss = { showBuildDateModal = false }
                    )
                }
                DefaultSpacer()
                OutlinedTextField(
                    value = mileage.toString(),
                    onValueChange = { value ->
                        mileage = value.toIntOrNull() ?: 0
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
                        },
                        onDismiss = { showLastUsedDateModal = false }
                    )
                }
                DefaultSpacer()
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
                                contentDescription = "Save the component"
                            )
                        },
                        onClick = {
                            val component = Component(
                                name = name,
                                description = description,
                                bikeUid = currentBike?.uid ?: 0,
                                parentComponentUid = currentParentComponent?.uid ?: 0,
                                acquisitionDate = selectedAcquisitionDate.toLocalDate()
                                    ?: Clock.System.todayIn(TimeZone.currentSystemDefault()),
                                mileage = mileage,
                                lastUsedDate = selectedLastUsedDate.toLocalDate(),
                                uid = 0
                            )
                            componentAddViewModel.saveNewComponent(component)
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

fun Long?.toLocalDate(): LocalDate? {
    if (this != null) {
        val lastUsedDateInstant = Instant.fromEpochMilliseconds(this)
        val lastUsedDate =
            lastUsedDateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return lastUsedDate
    }
    return null
}