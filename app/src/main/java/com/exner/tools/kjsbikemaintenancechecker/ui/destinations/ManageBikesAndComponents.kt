package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.ui.ManageBikesAndComponentsViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddComponentDestination
import com.ramcosta.composedestinations.generated.destinations.EditBikeDestination
import com.ramcosta.composedestinations.generated.destinations.EditComponentDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ManageBikesAndComponents(
    manageBikesAndComponentsViewModel: ManageBikesAndComponentsViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val bikes: List<Bike> by manageBikesAndComponentsViewModel.bikes.collectAsState(
        initial = emptyList()
    )

    val components: List<Component> by manageBikesAndComponentsViewModel.components.collectAsState(
        initial = emptyList()
    )

    val currentBike by manageBikesAndComponentsViewModel.currentBike.collectAsState()

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                Text(text = "Tap bike or component to edit or delete. Long press bike to select it.")
                DefaultSpacer()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    stickyHeader {
                        Text(
                            text = "Bikes",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    items(bikes, key = { "bike.${it.uid}" }) { bike ->
                        Surface(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        destinationsNavigator.navigate(EditBikeDestination(bike.uid))
                                    },
                                    onLongClick = {
                                        if (currentBike == bike.uid) {
                                            manageBikesAndComponentsViewModel.updateCurrentBike(-1L) // no bike
                                        } else {
                                            manageBikesAndComponentsViewModel.updateCurrentBike(bike.uid)
                                        }
                                    }
                                )
                                .padding(4.dp),
                            color = if (currentBike == bike.uid) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                                    contentDescription = "Bike",
                                )
                                IconSpacer()
                                Text(
                                    text = bike.name,
                                    modifier = Modifier.weight(1f)
                                )
                                DefaultSpacer()
                                Text(
                                    text = "${bike.mileage} km",
                                )
                                DefaultSpacer()
                                Text(
                                    text = if (bike.lastUsedDate != null) {
                                        bike.lastUsedDate.toString()
                                    } else {
                                        "not yet used"
                                    },
                                )
                            }
                        }
                    }

                    stickyHeader {
                        Text(
                            text = "Components",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    val filteredComponents = if (currentBike > 0) {
                        components.filter { component ->
                            component.bikeUid == currentBike
                        }
                    } else {
                        components
                    }

                    items(filteredComponents, key = { "component.${it.uid}" }) { component ->
                        Surface(
                            onClick = {
                                destinationsNavigator.navigate(EditComponentDestination(component.uid))
                            },
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Dataset, // TODO
                                    contentDescription = "Component",
                                )
                                IconSpacer()
                                Column {
                                    Text(
                                        text = component.name,
                                    )
                                    Text(
                                        text = component.description,
                                        style = MaterialTheme.typography.bodySmall
                                    )
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
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Add component") },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add component"
                            )
                        },
                        onClick = {
                            destinationsNavigator.navigate(AddComponentDestination)
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            )
        }
    )
}