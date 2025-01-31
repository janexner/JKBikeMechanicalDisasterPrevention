package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Hail
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivityWithBikeData
import com.exner.tools.kjsbikemaintenancechecker.ui.PrepareShortRideViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TodoListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareShortRideDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Destination<RootGraph>
@Composable
fun PrepareShortRide(
    prepareShortRideViewModel: PrepareShortRideViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val bikes: List<Bike> by prepareShortRideViewModel.observeBikesRaw.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    var currentBike: Bike? by remember { mutableStateOf(null) }

    val activitiesByBikes: List<ActivityWithBikeData> by prepareShortRideViewModel.observeActivitiesByBikes.collectAsState(
        initial = emptyList()
    )

    var modified by remember { mutableStateOf(false) }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                var offset = Offset.Zero
                var bikesExpanded by remember {
                    mutableStateOf(false)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp)
                        .wrapContentSize(Alignment.TopEnd)
                        .pointerInteropFilter {
                            offset = Offset(it.x, it.y)
                            false
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Which bike: ")
                        Button(
                            onClick = { bikesExpanded = true }
                        ) {
                            if (currentBike != null) {
                                Text(text = currentBike!!.name)
                            } else {
                                Text(text = "Select a bike")
                            }
                        }
                    }
                    val density = LocalDensity.current
                    DropdownMenu(
                        expanded = bikesExpanded,
                        offset = DpOffset(pxToDp(offset.x, density), pxToDp(offset.y, density)),
                        onDismissRequest = { bikesExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = "All bikes") },
                            onClick = {
                                currentBike = null
                                modified = true
                                bikesExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                        bikes.forEach { bike ->
                            DropdownMenuItem(
                                text = { Text(text = bike.name) },
                                onClick = {
                                    currentBike = bike
                                    modified = true
                                    bikesExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                DefaultSpacer()
                // filter activities by bike
                val suppressBikeBadge = (currentBike != null && currentBike!!.uid > 0)
                val filteredActivities = if (currentBike == null || currentBike!!.uid < 1) {
                    activitiesByBikes
                } else {
                    activitiesByBikes.filter { activityByBike ->
                        activityByBike.bikeName == currentBike!!.name || activityByBike.bikeName == null
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    stickyHeader {
                        Text(text = "TODOs for a quick ride:")
                    }

                    items(items = filteredActivities, key = { it.activityUid }) { activityByBike ->
                        val activity = Activity(
                            title = activityByBike.activityTitle,
                            description = activityByBike.activityDescription,
                            isCompleted = activityByBike.activityIsCompleted,
                            createdDate = activityByBike.activityCreatedDate,
                            dueDate = activityByBike.activityDueDate,
                            bikeUid = activityByBike.bikeUid!!,
                            uid = activityByBike.activityUid
                        )
                        TodoListItem(
                            activity = activityByBike,
                            destinationsNavigator = destinationsNavigator,
                            onCheckboxCallback = { result ->
                                prepareShortRideViewModel.updateActivity(activity = activity.copy(isCompleted = result))
                            },
                            suppressBikeBadge = suppressBikeBadge,
                            suppressDueDate = true
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(HomeDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.Home, contentDescription = "home")
                    },
                    label = { Text(text = "home") },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareShortRideDestination)
                    },
                    icon = {
                        Icon(Icons.Filled.ThumbUp, contentDescription = "quick ride")
                    },
                    label = { Text(text = "quick ride") },
                    selected = true
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareDayOutDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.Hail, contentDescription = "day out")
                    },
                    label = { Text(text = "day out") },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.Luggage, contentDescription = "holidays")
                    },
                    label = { Text(text = "holidays") },
                    selected = false
                )
            }
        }
    )
}

fun pxToDp(pixels: Float, density: Density): Dp {
    val dp = with(density) { pixels.toDp() }
    return dp
}
