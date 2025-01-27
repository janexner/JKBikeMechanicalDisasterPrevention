package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.ui.ManageBikesAndComponentsViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddBikeDestination
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

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                Text(text = "Tap bike or component to edit or delete.")
                DefaultSpacer()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    stickyHeader {
                        Text(
                            text = "Bikes",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 4.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    items(bikes, key = { "bike.${it.uid}" }) { bike ->
                        Surface(
                            onClick = {
                                destinationsNavigator.navigate(EditBikeDestination(bike.uid))
                            },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
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
                                    text = "${bike.lastUsedDate.toLocalDate()}",
                                )
                            }
                        }
                    }

                    stickyHeader {
                        Text(
                            text = "Components",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp, 0.dp, 4.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    items(components, key = { "component.${it.uid}" }) { component ->
                        Surface(
                            onClick = {
                                destinationsNavigator.navigate(EditComponentDestination(component.uid))
                            },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = component.name,
                                )
                                DefaultSpacer()
                                Text(
                                    text = component.description,
                                )
                            }
                        }
                    }
                }
//
//                Spacer(modifier = Modifier.weight(0.5f))
//
//                Button(
//                    modifier = Modifier.fillMaxWidth(),
//                    onClick = {
//                        destinationsNavigator.navigate(AddBikeDestination)
//                    }
//                ) {
//                    Text(text = "Add Bike")
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(
//                    modifier = Modifier.fillMaxWidth(),
//                    onClick = {
//                        destinationsNavigator.navigate(AddComponentDestination)
//                    }
//                ) {
//                    Text(text = "Add Component")
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
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

                    IconButton(
                        onClick = {
                            destinationsNavigator.navigate(AddBikeDestination)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Bike"
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