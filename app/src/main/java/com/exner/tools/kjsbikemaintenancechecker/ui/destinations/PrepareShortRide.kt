package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Hail
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.kjsbikemaintenancechecker.R
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivityWithBikeData
import com.exner.tools.kjsbikemaintenancechecker.ui.PrepareShortRideViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.ShowAnimatedText
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TodoListItem
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TransientTodoListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareShortRideDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
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

    val shortRideActivities by prepareShortRideViewModel.observeActivitiesShortRide.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    var modified by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.hdr_quick_ride),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    if (prepareShortRideViewModel.showIntroText.value) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = stringResource(R.string.collapse),
                            modifier = Modifier.clickable {
                                prepareShortRideViewModel.updateShowIntroText(false)
                            }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(R.string.expand),
                            modifier = Modifier.clickable {
                                prepareShortRideViewModel.updateShowIntroText(true)
                            }
                        )
                    }
                }
                ShowAnimatedText(show = prepareShortRideViewModel.showIntroText.value) {
                    DefaultSpacer()
                    Text(text = stringResource(R.string.quick_ride_you_are_not_so_far_away_from_a_walk_out_that_it_is_a_disaster_if_something_breaks))
                    DefaultSpacer()
                    Text(text = stringResource(R.string.these_activities_help_avoid_things_that_will_either_stop_you_from_riding_or_make_your_ride_miserable_if_not_right))
                    DefaultSpacer()
                    Text(text = stringResource(R.string.best_do_these_the_night_before))
                    DefaultSpacer()
                }
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
                        Text(text = stringResource(R.string.which_bike))
                        Button(
                            onClick = { bikesExpanded = true }
                        ) {
                            if (currentBike != null) {
                                Text(text = currentBike!!.name)
                            } else {
                                Text(text = stringResource(R.string.select_a_bike))
                            }
                        }
                    }
                    val density = LocalDensity.current
                    DropdownMenu(
                        expanded = bikesExpanded,
                        offset = DpOffset(pxToDp(offset.x, density), pxToDp(offset.y, density)),
                        onDismissRequest = { bikesExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.all_bikes)) },
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
                        .weight(0.5f)
                ) {
                    stickyHeader {
                        Text(text = stringResource(R.string.activities_for_a_quick_ride))
                    }

                    items(
                        items = shortRideActivities,
                        key = { "temp-${it.activityUid}" }) { activity ->
                        TransientTodoListItem(
                            activity = activity,
                            onCheckboxCallback = { checked ->
                                prepareShortRideViewModel.updateRideActivity(
                                    activityUid = activity.activityUid,
                                    isCompleted = checked
                                )
                            },
                            suppressDueDate = true
                        )
                    }

                    stickyHeader {
                        Text(text = stringResource(R.string.activities_that_are_due_anyway))
                    }

                    items(items = filteredActivities, key = { it.activityUid }) { activityByBike ->
                        val activity = Activity(
                            title = activityByBike.activityTitle,
                            description = activityByBike.activityDescription,
                            isCompleted = activityByBike.activityIsCompleted,
                            rideUid = null,
                            createdDate = activityByBike.activityCreatedDate,
                            dueDate = activityByBike.activityDueDate,
                            doneDate = activityByBike.activityDoneDate,
                            bikeUid = activityByBike.bikeUid!!,
                            uid = activityByBike.activityUid
                        )
                        TodoListItem(
                            activity = activityByBike,
                            destinationsNavigator = destinationsNavigator,
                            onCheckboxCallback = { result ->
                                prepareShortRideViewModel.updateActivity(
                                    activity = activity.copy(
                                        isCompleted = result,
                                        doneDate = if (result) {
                                            Clock.System.now()
                                        } else {
                                            null
                                        }
                                    )
                                )
                            },
                            suppressBikeBadge = suppressBikeBadge,
                            suppressDueDate = true
                        )
                    }
                }
                DefaultSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        prepareShortRideViewModel.endCurrentRideAndStartFromScratch()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear and start anew"
                        )
                        IconSpacer()
                        Text(text = "Start new short ride")
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
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = stringResource(R.string.tab_home)
                        )
                    },
                    label = { Text(text = stringResource(R.string.tab_home)) },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareShortRideDestination)
                    },
                    icon = {
                        Icon(
                            Icons.Filled.ThumbUp,
                            contentDescription = stringResource(R.string.tab_quick_ride)
                        )
                    },
                    label = { Text(text = stringResource(R.string.tab_quick_ride)) },
                    selected = true
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareDayOutDestination)
                    },
                    icon = {
                        Icon(
                            Icons.Outlined.Hail,
                            contentDescription = stringResource(R.string.tab_day_out)
                        )
                    },
                    label = { Text(text = stringResource(R.string.tab_day_out)) },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
                    },
                    icon = {
                        Icon(
                            Icons.Outlined.Luggage,
                            contentDescription = stringResource(R.string.tab_holidays)
                        )
                    },
                    label = { Text(text = stringResource(R.string.tab_holidays)) },
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
