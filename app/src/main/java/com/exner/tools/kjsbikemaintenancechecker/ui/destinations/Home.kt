package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.ui.HomeViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.destinations.wrappers.OnboardingWrapper
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareShortRideDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>(
    wrappers = [OnboardingWrapper::class]
)
@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val activities: List<Activity> by homeViewModel.observeActivitiesByDueDate.collectAsState(
        initial = emptyList()
    )

    Scaffold(
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                stickyHeader {
                    Text(text = "TODOs")
                }

                items(items = activities, key = { it.uid }) { activity ->
                    Surface(
                        modifier = Modifier
                            .clickable {
                                destinationsNavigator.navigate(
                                    ActivityDetailsDestination(
                                        activity.uid
                                    )
                                )
                            },
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(text = "${activity.dueDate} - ${activity.title}")
                            },
                            supportingContent = {
                                Text(text = activity.description)
                            },
                            trailingContent = {
                                Checkbox(
                                    checked = activity.isCompleted,
                                    onCheckedChange = {
                                        homeViewModel.updateActivity(activity.copy(isCompleted = !activity.isCompleted))
                                    }
                                )
                            }
                        )
                    }
                }

                // more UI

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
                        Icon(Icons.Outlined.ThumbUp, contentDescription = "day out")
                    },
                    label = { Text(text = "day out") },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.ThumbUp, contentDescription = "holidays")
                    },
                    label = { Text(text = "holidays") },
                    selected = false
                )
            }
        }
    )
}