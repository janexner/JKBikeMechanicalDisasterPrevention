package com.exner.tools.kjdoitnow.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import com.exner.tools.kjdoitnow.database.entities.Accessory
import com.exner.tools.kjdoitnow.database.entities.Bike
import com.exner.tools.kjdoitnow.ui.AccessoryEditViewModel
import com.exner.tools.kjdoitnow.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultParentAccessorySelectorWithSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.kjdoitnow.ui.components.PageHeaderTextWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AccessoryDeleteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Destination<RootGraph>
@Composable
fun AccessoryEdit(
    accessoryUid: Long,
    destinationsNavigator: DestinationsNavigator
) {

    val accessoryEditViewModel =
        hiltViewModel<AccessoryEditViewModel, AccessoryEditViewModel.AccessoryEditViewModelFactory> { factory ->
            factory.create(componentUid = accessoryUid)
        }

    val accessory by accessoryEditViewModel.accessory.observeAsState()
    val buildDateInstant = accessory?.let {
        LocalDateTime(it.acquisitionDate, LocalTime(12, 0, 0)).toInstant(
            TimeZone.currentSystemDefault()
        )
    }
    var selectedBuildDate = buildDateInstant?.toEpochMilliseconds()
    val lastUsedDateInstant = accessory?.let {
        it.lastUsedDate?.let { it1 ->
            LocalDateTime(it1, LocalTime(12, 0, 0)).toInstant(
                TimeZone.currentSystemDefault()
            )
        }
    }
    var selectedLastUsedDate = lastUsedDateInstant?.toEpochMilliseconds()

    val bikes: List<Bike> by accessoryEditViewModel.observeBikes.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentBike: Bike? by accessoryEditViewModel.currentBike.collectAsStateWithLifecycle(
        initialValue = null
    )

    val accessories: List<Accessory> by accessoryEditViewModel.observeAccessories.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentParentAccessory: Accessory? by accessoryEditViewModel.currentParentAccessory.collectAsStateWithLifecycle(
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
                PageHeaderTextWithSpacer(stringResource(R.string.edit_an_accessory))
                DefaultTextFieldWithSpacer(
                    value = accessory?.name ?: stringResource(R.string.placehldr_name),
                    onValueChange = {
                        accessoryEditViewModel.updateName(it)
                        modified = true
                    },
                    label = stringResource(R.string.lbl_accessory_name),
                )
                DefaultTextFieldWithSpacer(
                    value = accessory?.description ?: stringResource(R.string.placehldr_description),
                    onValueChange = {
                        accessoryEditViewModel.updateDescription(it)
                        modified = true
                    },
                    label = stringResource(R.string.lbl_description),
                )
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike == null) stringResource(R.string.none) else currentBike!!.name,
                    label = stringResource(R.string.lbl_attached_to_bike),
                    onMenuItemClick = {
                        accessoryEditViewModel.updateAttachedBike(it)
                        modified = true
                    },
                    bikes = bikes
                )
                DefaultParentAccessorySelectorWithSpacer(
                    value = if (currentParentAccessory == null) stringResource(R.string.none) else currentParentAccessory!!.name,
                    label = stringResource(R.string.lbl_part_of_accessory),
                    onMenuItemClick = {
                        accessoryEditViewModel.updateParentComponent(it)
                        modified = true
                    },
                    accessories = accessories
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedBuildDate,
                    label = stringResource(R.string.lbl_acquisition_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedBuildDate = it
                        if (it != null) {
                            accessoryEditViewModel.updateAcquisitionDate(it)
                        }
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedLastUsedDate,
                    label = stringResource(R.string.lbl_last_used_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedLastUsedDate = it
                        accessoryEditViewModel.updateLastUsedDate(it)
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
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                    IconButton(onClick = {
                        destinationsNavigator.navigate(AccessoryDeleteDestination(accessoryUid))
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
                            accessoryEditViewModel.commitAccessory()
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