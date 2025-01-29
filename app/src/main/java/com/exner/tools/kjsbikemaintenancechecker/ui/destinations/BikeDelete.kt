package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.BikeDeleteViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TextAndSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ManageBikesAndComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun BikeDelete(
    bikeUid: Long,
    destinationsNavigator: DestinationsNavigator
) {
    val bikeDeleteViewModel =
        hiltViewModel<BikeDeleteViewModel, BikeDeleteViewModel.BikeDeleteViewModelFactory> { factory ->
            factory.create(bikeUid = bikeUid)
        }

    val bike by bikeDeleteViewModel.bike.observeAsState()
    val componentCount by bikeDeleteViewModel.componentCount.observeAsState()
    var deleteAttachedComponents by remember { mutableStateOf(false) }
    val activityCount by bikeDeleteViewModel.activityCount.observeAsState()
    var deleteAttachedActivities by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).padding(8.dp)
            ) {
                if (bike != null) {
                    Text(text = "You are about to delete the bike ${bike!!.name}.")
                    if (componentCount != null && componentCount!! > 0) {
                        DefaultSpacer()
                        TextAndSwitch(
                            text = "Delete attached components ($componentCount)",
                            checked = deleteAttachedComponents
                        ) {
                            deleteAttachedComponents = it
                        }
                    }
                    if (activityCount != null && activityCount!! > 0) {
                        DefaultSpacer()
                        TextAndSwitch(
                            text = "Delete attached activities ($activityCount)",
                            checked = deleteAttachedActivities
                        ) {
                            deleteAttachedActivities = it
                        }
                    }
                } else {
                    Text(text = "We did not find this bike.")
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
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Delete") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Delete the bike"
                                )
                            },
                            onClick = {
                                bikeDeleteViewModel.commitDelete(
                                    deleteAttachedComponents = deleteAttachedComponents,
                                    deleteAttachedActivities = deleteAttachedActivities,
                                )
                                destinationsNavigator.popBackStack(ManageBikesAndComponentsDestination, inclusive = false)
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                }
            )
        }
    )
}
