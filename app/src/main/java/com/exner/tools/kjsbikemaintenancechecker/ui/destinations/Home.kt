package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.Activity
import com.exner.tools.kjsbikemaintenancechecker.ui.ButtonWithIconAndText
import com.exner.tools.kjsbikemaintenancechecker.ui.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.HomeViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.destinations.wrappers.OnboardingWrapper
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination
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

    val activities: List<Activity> by homeViewModel.observeActivitiesByDueDate.collectAsState(initial = emptyList())

    Scaffold(
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                item {
                    Text(text = "Welcome to KJ's Bike Maintenance Checker!")
                }

                stickyHeader {
                    Text(text = "Upcoming tasks")
                }

                items(activities) { activity ->
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
                                Text(text = activity.title)
                            },
                            supportingContent = {
                                Text(text = activity.description)
                            }
                        )
                    }
                }

                // more UI

            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    ButtonWithIconAndText(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "day out",
                        onClick = {
                            destinationsNavigator.navigate(PrepareDayOutDestination)
                        }
                    )
                    ButtonWithIconAndText(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "bike holidays",
                        onClick = {
                            destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
                        }
                    )
                    ButtonWithIconAndText(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "add component",
                        onClick = {
                            // TODO
                        }
                    )

                },
                floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "short ride") },
                            icon = {
                                Icon(Icons.Default.PlayArrow, "Prepare for a short ride")
                            },
                            onClick = {
                                 destinationsNavigator.navigate(PrepareShortRideDestination)
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                }
            )
        }
    )
}