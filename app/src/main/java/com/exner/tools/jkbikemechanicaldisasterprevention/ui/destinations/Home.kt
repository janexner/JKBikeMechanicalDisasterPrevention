package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Hail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
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
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.HomeViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.NavigationStyle
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TodoListItem
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsMenuItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityAddDestination
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareQuickRideDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock

@Destination<RootGraph>(start = true)
@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val listOfMenuItems: List<KJsMenuItem> = listOf(
        KJsMenuItem(
            label = stringResource(R.string.tab_home),
            icon = Icons.Default.Home,
            selected = true,
            onClick = {
                destinationsNavigator.navigate(HomeDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_quick_ride),
            icon = Icons.Default.ThumbUp,
            selected = false,
            onClick = {
                destinationsNavigator.navigate(PrepareQuickRideDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_day_out),
            icon = Icons.Default.Hail,
            selected = false,
            onClick = {
                destinationsNavigator.navigate(PrepareDayOutDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_holidays),
            icon = Icons.Default.Luggage,
            selected = false,
            onClick = {
                destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
            }
        ),
    )

    val navigationStyle = NavigationStyle.LEFT_DRAWER

    when (navigationStyle) {
        NavigationStyle.BOTTOM_BAR -> {
            Scaffold(
                modifier = Modifier.imePadding(),
                content = { innerPadding ->
                    HomeContent(
                        innerPadding,
                        destinationsNavigator,
                        homeViewModel
                    )
                },
                bottomBar = {
                    NavigationBar {
                        listOfMenuItems.forEach { item ->
                            NavigationBarItem(
                                selected = item.selected,
                                onClick = item.onClick,
                                label = {
                                    Text(text = item.label)
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                },
                            )
                        }
                    }
                }
            )
        }

        NavigationStyle.LEFT_RAIL -> {
            Row(
                modifier = Modifier.imePadding()
            ) {
                NavigationRail(
                    containerColor = NavigationBarDefaults.containerColor
                ) {
                    listOfMenuItems.forEach { item ->
                        NavigationRailItem(
                            selected = item.selected,
                            onClick = item.onClick,
                            label = {
                                Text(text = item.label)
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.5f))
                    NavigationRailItem(
                        selected = false,
                        onClick = {
                            destinationsNavigator.navigate(SettingsDestination)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.menu_item_settings)
                            )
                        },
                        label = {
                            Text(stringResource(R.string.menu_item_settings))
                        }
                    )

                }
                DefaultSpacer()
                HomeContent(
                    PaddingValues(8.dp),
                    destinationsNavigator,
                    homeViewModel
                )
            }
        }

        NavigationStyle.LEFT_DRAWER -> {
            PermanentNavigationDrawer(
                modifier = Modifier,
                drawerContent = {
                    PermanentDrawerSheet(
                        modifier = Modifier.width(180.dp),
                        drawerContainerColor = DrawerDefaults.standardContainerColor
                    ) {
                        Column {
                            listOfMenuItems.forEach { item ->
                                NavigationDrawerItem(
                                    selected = item.selected,
                                    onClick = item.onClick,
                                    label = {
                                        Text(text = item.label)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.label
                                        )
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.weight(0.5f))
                            NavigationDrawerItem(
                                selected = false,
                                onClick = {
                                    destinationsNavigator.navigate(SettingsDestination)
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = stringResource(R.string.menu_item_settings)
                                    )
                                },
                                label = {
                                    Text(stringResource(R.string.menu_item_settings))
                                }
                            )
                        }
                    }
                },
            ) {
                HomeContent(
                    PaddingValues(8.dp),
                    destinationsNavigator,
                    homeViewModel
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeContent(
    innerPadding: PaddingValues,
    destinationsNavigator: DestinationsNavigator,
    homeViewModel: HomeViewModel
) {
    val activitiesByBikes: List<ActivityWithBikeData> by homeViewModel.observeActivityWithBikeData.collectAsState(
        initial = emptyList()
    )

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.welcome_to_kj_do_it_now))
        DefaultSpacer()
        val filteredActivitiesByBikes: List<ActivityWithBikeData> =
            activitiesByBikes.filter { activityWithBikeData ->
                activityWithBikeData.rideUid == null || activityWithBikeData.rideUid < 0
            }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp),
                    text = stringResource(R.string.todos)
                )
            }

            items(
                items = filteredActivitiesByBikes,
                key = { it.activityUid }) { activityByBike ->
                val activity = Activity(
                    title = activityByBike.activityTitle,
                    description = activityByBike.activityDescription,
                    isCompleted = activityByBike.activityIsCompleted,
                    rideUid = null,
                    isEBikeSpecific = activityByBike.isEBikeSpecific,
                    rideLevel = activityByBike.activityRideLevel,
                    createdInstant = activityByBike.activityCreatedInstant,
                    dueDate = activityByBike.activityDueDate,
                    bikeUid = activityByBike.bikeUid ?: 0,
                    doneInstant = activityByBike.activityDoneDateInstant,
                    uid = activityByBike.activityUid
                )
                TodoListItem(
                    activity = activityByBike,
                    destinationsNavigator = destinationsNavigator,
                    onCheckboxCallback = { result ->
                        homeViewModel.updateActivity(
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
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // more static UI
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                destinationsNavigator.navigate(ActivityAddDestination)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.AddTask,
                    contentDescription = stringResource(R.string.add_activity)
                )
                IconSpacer()
                Text(text = stringResource(R.string.add_activity))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}