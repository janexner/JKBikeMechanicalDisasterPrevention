package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.BuildConfig
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.HomeViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.ComponentWearLevelDialog
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TodoListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityAddDestination
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>(start = true)
@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        HomeDestination,
        destinationsNavigator,
        windowSizeClass
    ) {
        val activitiesByBikes: List<ActivityWithBikeData> by homeViewModel
            .observeActivityWithBikeData.collectAsState(
                initial = emptyList()
            )
        var showDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(PaddingValues(8.dp))
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
                        text = stringResource(R.string.hdr_todos)
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
                        componentUid = activityByBike.activityComponentUid,
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
                            // popup in case this has a component attached
                            if (activityByBike.activityComponentUid != null) {
                                // update component
                                homeViewModel.updateAttachedComponent(
                                    componentUid = activityByBike.activityComponentUid,
                                    bikeUid = activityByBike.bikeUid
                                )
                                if (result) {
                                    // show UI for component wearlevel
                                    showDialog = true
                                }
                            }
                        },
                    )
                    if (showDialog) {
                        ComponentWearLevelDialog(
                            onDismissRequest = {
                                showDialog = false
                            },
                            onConfirmation = { wearLevel ->
                                showDialog = false
                                // write stuff
                                if (activityByBike.activityComponentUid != null) {
                                    homeViewModel.logComponentWearLevel(
                                        componentUid = activityByBike.activityComponentUid,
                                        wearLevel = wearLevel
                                    )
                                }
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            if (BuildConfig.DEBUG) {
                val context = LocalContext.current

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        homeViewModel.debugCheckAndCreate(context = context)
                    }) {
                        Text(text = "Debug")
                    }
                }
            }

            // more static UI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        destinationsNavigator.navigate(ActivityAddDestination)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddTask,
                            contentDescription = stringResource(R.string.btn_desc_add_activity)
                        )
                        IconSpacer()
                        Text(text = stringResource(R.string.btn_text_add_activity))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
