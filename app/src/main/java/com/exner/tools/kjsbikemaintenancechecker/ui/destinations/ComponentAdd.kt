package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.ui.ComponentAddViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultParentComponentSelectorWithSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultTextFieldWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

@Destination<RootGraph>
@Composable
fun ComponentAdd(
    bikeUid: Long?,
    destinationsNavigator: DestinationsNavigator
) {

    val componentAddViewModel =
        hiltViewModel<ComponentAddViewModel, ComponentAddViewModel.ComponentAddViewModelFactory> { factory ->
            factory.create(bikeUid = bikeUid ?: 0)
        }

    // input fields
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attachedBikeUid by remember { mutableStateOf(bikeUid) }
    var parentComponentUid by remember { mutableStateOf<Long?>(null) }
    var selectedAcquisitionDate by remember { mutableStateOf<Long?>(null) }
    var mileage by remember { mutableIntStateOf(0) }
    var selectedLastUsedDate by remember { mutableStateOf<Long?>(null) }

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
                DefaultTextFieldWithSpacer(
                    value = name,
                    onValueChange = {
                        name = it
                        modified = true
                    },
                    label = "Component name",
                )
                DefaultTextFieldWithSpacer(
                    value = description,
                    onValueChange = {
                        description = it
                        modified = true
                    },
                    label = "Description",
                )
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike == null) "None" else currentBike!!.name,
                    label = "Attached to bike",
                    onMenuItemClick = {
                        componentAddViewModel.updateAttachedBike(null)
                        attachedBikeUid = null
                        modified = true
                    },
                    bikes = bikes
                )
                DefaultParentComponentSelectorWithSpacer(
                    value = if (currentParentComponent == null) "None" else currentParentComponent!!.name,
                    label = "Part of component",
                    onMenuItemClick = {
                        componentAddViewModel.updateParentComponent(null)
                        parentComponentUid = null
                        modified = true
                    },
                    components = components
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedAcquisitionDate,
                    label = "Acquisition date",
                    placeholder = "DD/MM/YYYY",
                    onDateSelected = {
                        selectedAcquisitionDate = it
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = mileage.toString(),
                    onValueChange = { value ->
                        mileage = value.toIntOrNull() ?: 0
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
                    }
                )
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