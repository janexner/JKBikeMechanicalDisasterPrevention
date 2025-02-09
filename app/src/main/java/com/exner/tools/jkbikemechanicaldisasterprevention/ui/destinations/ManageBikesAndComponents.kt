package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ManageBikesAndComponentsViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeEditDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentAddDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ManageBikesAndComponents(
    manageBikesAndComponentsViewModel: ManageBikesAndComponentsViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val flattenedComponents by manageBikesAndComponentsViewModel.flattenedBikesAndComponents.collectAsState()

    val cidList by manageBikesAndComponentsViewModel.cidList.collectAsState()

    Log.d("MBC", "List of collapsed ids: $cidList")
    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                PageHeaderTextWithSpacer(stringResource(R.string.manage_bikes_components))
                Text(text = stringResource(R.string.tap_bike_or_component_to_edit_or_delete_long_press_bike_to_select_it))
                DefaultSpacer()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(flattenedComponents) { bikeOrComponent ->
                        // is this hidden?
                        if (!manageBikesAndComponentsViewModel.isThisIdHidden(bikeOrComponent.collapseIdTags, cidList)) {
                            Surface(onClick = {
                                if (bikeOrComponent.isBike()) {
                                    destinationsNavigator.navigate(
                                        BikeEditDestination(bikeOrComponent.bike!!.uid)
                                    )
                                } else if (bikeOrComponent.isComponent() && bikeOrComponent.component != null) {
                                    destinationsNavigator.navigate(
                                        ComponentEditDestination(bikeOrComponent.component.uid)
                                    )
                                }
                            }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = (24 * bikeOrComponent.level).dp,
                                            top = 0.dp,
                                            end = 0.dp,
                                            bottom = 0.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Log.d("MBC", "Showing $bikeOrComponent")
                                    if (bikeOrComponent.isBike()) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                                            contentDescription = "Bike"
                                        )
                                        IconSpacer()
                                        Text(text = bikeOrComponent.bike!!.name)
                                        DefaultSpacer()
                                        Text(
                                            text = "${bikeOrComponent.bike.mileage} km",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(modifier = Modifier.weight(0.5f))
                                        Text(
                                            text = "${bikeOrComponent.bike.lastUsedDate}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        IconSpacer()
                                        IconButton(onClick = {
                                            Log.d("MBC", "Tap! ${bikeOrComponent.collapseId} - ${manageBikesAndComponentsViewModel.isThisIdCollapsed(bikeOrComponent.collapseId, cidList)}")
                                            if (manageBikesAndComponentsViewModel.isThisIdCollapsed(bikeOrComponent.collapseId, cidList)) {
                                                manageBikesAndComponentsViewModel.removeIdFromCollapsedIds(
                                                    bikeOrComponent.collapseId
                                                )
                                            } else {
                                                manageBikesAndComponentsViewModel.addIdToCollapsedIds(
                                                    bikeOrComponent.collapseId
                                                )
                                            }
                                        }) {
                                            if (bikeOrComponent.hasChildren) {
                                                Icon(
                                                    imageVector = if (manageBikesAndComponentsViewModel.isThisIdCollapsed(bikeOrComponent.collapseId, cidList)) { Icons.Default.ArrowDropDown } else { Icons.Default.ArrowDropUp },
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    } else if (bikeOrComponent.isComponent() && bikeOrComponent.component != null) {
                                        Icon(
                                            imageVector = Icons.Default.Dataset, // TODO
                                            contentDescription = stringResource(R.string.component),
                                        )
                                        IconSpacer()
                                        Column(
                                            modifier = Modifier.weight(.8f)
                                        ) {
                                            Text(text = bikeOrComponent.component.name)
                                            Text(
                                                text = bikeOrComponent.component.description,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        IconSpacer()
                                        IconButton(onClick = {
                                            Log.d("MBC", "Tap! ${bikeOrComponent.collapseId} - ${manageBikesAndComponentsViewModel.isThisIdCollapsed(bikeOrComponent.collapseId, cidList)}")
                                            if (manageBikesAndComponentsViewModel.isThisIdCollapsed(bikeOrComponent.collapseId, cidList)) {
                                                manageBikesAndComponentsViewModel.removeIdFromCollapsedIds(
                                                    bikeOrComponent.collapseId
                                                )
                                            } else {
                                                manageBikesAndComponentsViewModel.addIdToCollapsedIds(
                                                    bikeOrComponent.collapseId
                                                )
                                            }
                                        }) {
                                            if (bikeOrComponent.hasChildren) {
                                                Icon(
                                                    imageVector = if (manageBikesAndComponentsViewModel.isThisIdCollapsed(bikeOrComponent.collapseId, cidList)) { Icons.Default.ArrowDropDown } else { Icons.Default.ArrowDropUp },
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Error,
                                            contentDescription = "Error"
                                        )
                                        IconSpacer()
                                        Text(text = "This line is wrong $bikeOrComponent")
                                    }
                                }
                            }
                        }
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(R.string.add_a_component)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add_a_component)
                            )
                        },
                        onClick = {
                            destinationsNavigator.navigate(ComponentAddDestination(null))
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            )
        }
    )
}