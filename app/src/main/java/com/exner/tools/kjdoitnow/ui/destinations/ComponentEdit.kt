package com.exner.tools.kjdoitnow.ui.destinations

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.kjdoitnow.R
import com.exner.tools.kjdoitnow.database.entities.Bike
import com.exner.tools.kjdoitnow.database.entities.Component
import com.exner.tools.kjdoitnow.ui.ComponentEditViewModel
import com.exner.tools.kjdoitnow.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultNumberFieldWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultParentComponentSelectorWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.kjdoitnow.ui.components.PageHeaderTextWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ComponentDeleteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Destination<RootGraph>
@Composable
fun ComponentEdit(
    componentUid: Long,
    destinationsNavigator: DestinationsNavigator
) {

    val componentEditViewModel =
        hiltViewModel<ComponentEditViewModel, ComponentEditViewModel.ComponentEditViewModelFactory> { factory ->
            factory.create(componentUid = componentUid)
        }

    val component by componentEditViewModel.component.observeAsState()
    val buildDateInstant = component?.let {
        LocalDateTime(it.acquisitionDate, LocalTime(12, 0, 0)).toInstant(
            TimeZone.currentSystemDefault()
        )
    }
    var selectedBuildDate = buildDateInstant?.toEpochMilliseconds()
    val lastUsedDateInstant = component?.let {
        it.lastUsedDate?.let { it1 ->
            LocalDateTime(it1, LocalTime(12, 0, 0)).toInstant(
                TimeZone.currentSystemDefault()
            )
        }
    }
    var selectedLastUsedDate = lastUsedDateInstant?.toEpochMilliseconds()

    val bikes: List<Bike> by componentEditViewModel.observeBikes.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentBike: Bike? by componentEditViewModel.currentBike.collectAsStateWithLifecycle(
        initialValue = null
    )

    val components: List<Component> by componentEditViewModel.observeComponents.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentParentComponent: Component? by componentEditViewModel.currentParentComponent.collectAsStateWithLifecycle(
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
                PageHeaderTextWithSpacer(stringResource(R.string.edit_a_component))
                DefaultTextFieldWithSpacer(
                    value = component?.name ?: stringResource(R.string.placehldr_name),
                    onValueChange = {
                        componentEditViewModel.updateName(it)
                        modified = true
                    },
                    label = stringResource(R.string.lbl_component_name),
                )
                DefaultTextFieldWithSpacer(
                    value = component?.description ?: stringResource(R.string.placehldr_description),
                    onValueChange = {
                        componentEditViewModel.updateDescription(it)
                        modified = true
                    },
                    label = stringResource(R.string.lbl_description),
                )
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike == null) stringResource(R.string.none) else currentBike!!.name,
                    label = stringResource(R.string.lbl_attached_to_bike),
                    onMenuItemClick = {
                        componentEditViewModel.updateAttachedBike(it)
                        modified = true
                    },
                    bikes = bikes
                )
                DefaultParentComponentSelectorWithSpacer(
                    value = if (currentParentComponent == null) stringResource(R.string.none) else currentParentComponent!!.name,
                    label = stringResource(R.string.lbl_part_of_component),
                    onMenuItemClick = {
                        componentEditViewModel.updateParentComponent(it)
                        modified = true
                    },
                    components = components
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedBuildDate,
                    label = stringResource(R.string.lbl_acquisition_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedBuildDate = it
                        if (it != null) {
                            componentEditViewModel.updateAcquisitionDate(it)
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DefaultNumberFieldWithSpacer(
                        modifier = Modifier.weight(0.6f),
                        value = component?.mileage.toString(),
                        onValueChange = { value ->
                            componentEditViewModel.updateMileage(value.toIntOrNull() ?: 0)
                            modified = true
                        },
                        label = stringResource(R.string.lbl_mileage),
                    )
                    if (component?.mileage == 0 && currentBike != null) {
                        DefaultSpacer()
                        Button(
                            modifier = Modifier.weight(.4f),
                            onClick = {
                                componentEditViewModel.updateMileage(currentBike!!.mileage)
                            }
                        ) {
                            Text(text = "from bike")
                        }
                    }
                }
                DefaultSpacer()
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedLastUsedDate,
                    label = stringResource(R.string.lbl_last_used_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedLastUsedDate = it
                        componentEditViewModel.updateLastUsedDate(it)
                    }
                )
                DefaultNumberFieldWithSpacer(
                    value = component?.expectedLifespanInKm.toString(),
                    onValueChange = { value ->
                        componentEditViewModel.updateExpectedLifetimeInKm(value.toIntOrNull() ?: 0)
                        modified = true
                    },
                    label = stringResource(R.string.lblExpectedLifetimeInKm)
                )
                DefaultTextFieldWithSpacer(
                    value = component?.notes ?: "",
                    onValueChange = {
                        componentEditViewModel.updateNotes(it)
                        modified = true
                    },
                    label = stringResource(R.string.notes)
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
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                    IconButton(onClick = {
                        destinationsNavigator.navigate(ComponentDeleteDestination(componentUid))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(R.string.save)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.save_the_component)
                            )
                        },
                        onClick = {
                            componentEditViewModel.commitComponent()
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