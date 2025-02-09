package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

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
import androidx.compose.material.icons.filled.Hail
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.PrepareDayOutViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.ShowAnimatedText
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TodoListItem
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TransientTodoListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareQuickRideDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Destination<RootGraph>
@Composable
fun PrepareDayOut(
    prepareDayOutViewModel: PrepareDayOutViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val bikes: List<Bike> by prepareDayOutViewModel.observeBikesRaw.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    var currentBike: Bike? by remember { mutableStateOf(null) }
    var currentBikeIsAnEBike: Boolean by remember { mutableStateOf(true) }

    val activitiesByBikes: List<ActivityWithBikeData> by prepareDayOutViewModel.observeActivitiesByBikes.collectAsState(
        initial = emptyList()
    )

    val dayOutActivities by prepareDayOutViewModel.observeActivitiesDayOut.collectAsStateWithLifecycle(
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
                    PageHeaderTextWithSpacer(stringResource(R.string.hdr_day_out))
                    if (prepareDayOutViewModel.showIntroText.value) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = stringResource(R.string.collapse),
                            modifier = Modifier.clickable {
                                prepareDayOutViewModel.updateShowIntroText(false)
                            }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(R.string.expand),
                            modifier = Modifier.clickable {
                                prepareDayOutViewModel.updateShowIntroText(true)
                            }
                        )
                    }
                }
                ShowAnimatedText(show = prepareDayOutViewModel.showIntroText.value) {
                    DefaultSpacer()
                    Text(text = stringResource(R.string.day_out_definition))
                    DefaultSpacer()
                    Text(text = stringResource(R.string.day_out_avoid))
                    DefaultSpacer()
                }
                Text(text = stringResource(R.string.day_out_when_todo))
                DefaultSpacer()
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
                        DefaultSpacer()
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
                                currentBikeIsAnEBike = true
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
                                    currentBikeIsAnEBike = bike.isElectric
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
                val filteredDayOutActivities = if (currentBikeIsAnEBike) {
                    dayOutActivities
                } else {
                    dayOutActivities.filter { activityWithBikeData ->
                        !activityWithBikeData.isEBikeSpecific
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    stickyHeader {
                        Text(text = stringResource(R.string.activities_for_a_day_out))
                    }

                    items(
                        items = filteredDayOutActivities,
                        key = { "temp-${it.activityUid}" }) { activity ->
                        TransientTodoListItem(
                            activity = activity,
                            onCheckboxCallback = { checked ->
                                prepareDayOutViewModel.updateRideActivity(
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
                            createdInstant = activityByBike.activityCreatedInstant,
                            dueDate = activityByBike.activityDueDate,
                            doneInstant = activityByBike.activityDoneDateInstant,
                            bikeUid = activityByBike.bikeUid!!,
                            isEBikeSpecific = activityByBike.isEBikeSpecific,
                            rideLevel = activityByBike.activityRideLevel,
                            uid = activityByBike.activityUid
                        )
                        TodoListItem(
                            activity = activityByBike,
                            destinationsNavigator = destinationsNavigator,
                            onCheckboxCallback = { result ->
                                prepareDayOutViewModel.updateActivity(
                                    activity = activity.copy(
                                        isCompleted = result,
                                        doneInstant = if (result) {
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
                        prepareDayOutViewModel.endCurrentRideAndStartFromScratch()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_and_start_anew)
                        )
                        IconSpacer()
                        Text(text = stringResource(R.string.lbl_start_new_day_out))
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.popBackStack(HomeDestination, inclusive = false)
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
                        destinationsNavigator.navigate(PrepareQuickRideDestination)
                    },
                    icon = {
                        Icon(
                            Icons.Outlined.ThumbUp,
                            contentDescription = stringResource(R.string.tab_quick_ride)
                        )
                    },
                    label = { Text(text = stringResource(R.string.tab_quick_ride)) },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Filled.Hail,
                            contentDescription = stringResource(R.string.tab_day_out)
                        )
                    },
                    label = { Text(text = stringResource(R.string.tab_day_out)) },
                    selected = true,
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