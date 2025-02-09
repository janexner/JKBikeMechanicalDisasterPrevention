package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Accessory
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.AccessoryAddViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultBikeSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorNullableWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultDateSelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultParentAccessorySelectorWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.toLocalDate
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Destination<RootGraph>
@Composable
fun AccessoryAdd(
    accessoryAddViewModel: AccessoryAddViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    // input fields
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attachedBikeUid: Long? by remember { mutableStateOf(null) }
    var parentAccessoryUid by remember { mutableStateOf<Long?>(null) }
    var selectedAcquisitionDate by remember { mutableStateOf<Long?>(null) }
    var selectedLastUsedDate by remember { mutableStateOf<Long?>(null) }

    val bikes: List<Bike> by accessoryAddViewModel.observeBikes.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentBike: Bike? by accessoryAddViewModel.currentBike.collectAsStateWithLifecycle(
        initialValue = null
    )

    val accessories: List<Accessory> by accessoryAddViewModel.observeAccessories.collectAsStateWithLifecycle(
        emptyList()
    )
    val currentParentAccessory: Accessory? by accessoryAddViewModel.currentParentAccessory.collectAsStateWithLifecycle(
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
                PageHeaderTextWithSpacer(stringResource(R.string.add_an_accessory))
                Text(text = stringResource(R.string.a_new_accessory))
                DefaultSpacer()
                DefaultTextFieldWithSpacer(
                    value = name,
                    onValueChange = {
                        name = it
                        modified = true
                    },
                    label = stringResource(R.string.lbl_accessory_name),
                )
                DefaultTextFieldWithSpacer(
                    value = description,
                    onValueChange = {
                        description = it
                        modified = true
                    },
                    label = stringResource(R.string.lbl_description),
                )
                DefaultBikeSelectorWithSpacer(
                    value = if (currentBike == null) stringResource(R.string.none) else currentBike!!.name,
                    label = stringResource(R.string.lbl_attached_to_bike),
                    onMenuItemClick = {
                        accessoryAddViewModel.updateAttachedBike(null)
                        attachedBikeUid = null
                        modified = true
                    },
                    bikes = bikes
                )
                DefaultParentAccessorySelectorWithSpacer(
                    value = if (currentParentAccessory == null) stringResource(R.string.none) else currentParentAccessory!!.name,
                    label = stringResource(R.string.lbl_part_of_accessory),
                    onMenuItemClick = {
                        accessoryAddViewModel.updateParentAccessory(null)
                        parentAccessoryUid = null
                        modified = true
                    },
                    accessories = accessories
                )
                DefaultDateSelectorWithSpacer(
                    selectedDate = selectedAcquisitionDate,
                    label = stringResource(R.string.lbl_acquisition_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
                    onDateSelected = {
                        selectedAcquisitionDate = it
                    }
                )
                DefaultDateSelectorNullableWithSpacer(
                    selectedDate = selectedLastUsedDate,
                    label = stringResource(R.string.lbl_last_used_date),
                    placeholder = stringResource(R.string.placehldr_yyyy_mm_dd),
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
                            contentDescription = stringResource(R.string.cancel)
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
                            val accessory = Accessory(
                                name = name,
                                description = description,
                                bikeUid = currentBike?.uid ?: 0,
                                parentAccessoryUid = currentParentAccessory?.uid ?: 0,
                                acquisitionDate = selectedAcquisitionDate.toLocalDate()
                                    ?: Clock.System.todayIn(TimeZone.currentSystemDefault()),
                                lastUsedDate = selectedLastUsedDate.toLocalDate(),
                                uid = 0
                            )
                            accessoryAddViewModel.saveNewAccessory(accessory)
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
