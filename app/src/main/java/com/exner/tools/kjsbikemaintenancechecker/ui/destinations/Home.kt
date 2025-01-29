package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Hail
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.views.ActivitiesByBikes
import com.exner.tools.kjsbikemaintenancechecker.ui.HomeViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TodoListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityAddDestination
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareShortRideDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>(start = true)
@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val activitiesByBikes: List<ActivitiesByBikes> by homeViewModel.observeActivitiesByBikesWithDueDate.collectAsState(
        initial = emptyList()
    )

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    stickyHeader {
                        Text(text = "TODOs")
                    }

                    items(items = activitiesByBikes, key = { it.activityUid }) { activityByBike ->
                        val activity = Activity(
                            title = activityByBike.activityTitle,
                            description = activityByBike.activityDescription,
                            isCompleted = activityByBike.activityIsCompleted,
                            createdDate = activityByBike.activityCreatedDate,
                            dueDate = activityByBike.activityDueDate,
                            uid = activityByBike.activityUid
                        )
                        TodoListItem(
                            activity = activityByBike,
                            destinationsNavigator = destinationsNavigator,
                            onCheckboxCallback = { result ->
                                homeViewModel.updateActivity(activity = activity.copy(isCompleted = result))
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
                            contentDescription = "Add activity"
                        )
                        IconSpacer()
                        Text(text = "Add activity")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(HomeDestination)
                    },
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "home")
                    },
                    label = { Text(text = "home") },
                    selected = true
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareShortRideDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.ThumbUp, contentDescription = "quick ride")
                    },
                    label = { Text(text = "quick ride") },
                    selected = false
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