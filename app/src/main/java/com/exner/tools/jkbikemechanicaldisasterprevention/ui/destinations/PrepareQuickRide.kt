package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.ActivityWithBikeData
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.PrepareQuickRideViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.BikeSelector
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TodoListItem
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TransientTodoListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.PrepareQuickRideDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock

@OptIn(
    ExperimentalFoundationApi::class
)
@Destination<RootGraph>
@Composable
fun PrepareQuickRide(
    prepareQuickRideViewModel: PrepareQuickRideViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        PrepareQuickRideDestination,
        destinationsNavigator,
        windowSizeClass,
        headline = stringResource(R.string.hdr_quick_ride)
    ) {
        val bikes: List<Bike> by prepareQuickRideViewModel.observeBikesRaw.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )
        var currentBike: Bike? by remember { mutableStateOf(null) }
        var currentBikeIsAnEBike: Boolean by remember { mutableStateOf(true) }
        val activitiesByBikes: List<ActivityWithBikeData> by prepareQuickRideViewModel.observeActivitiesByBikes.collectAsState(
            initial = emptyList()
        )
        val shortRideActivities by prepareQuickRideViewModel.observeActivitiesShortRide.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )
        var modified by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 8.dp)
        ) {
            // on wide screens, we'll put header and bike selector next to each other
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(0.6f)
                ) {
                    Text(text = stringResource(R.string.quick_ride_when_todo))
                }
                // the bike selector
                BikeSelector(
                    currentBike = currentBike,
                    bikes = bikes,
                    displayLabel = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
                ) { bike ->
                    if (bike != currentBike) {
                        if (bike == null) {
                            currentBike = null
                            currentBikeIsAnEBike = false
                            modified = true
                        } else {
                            currentBike = bike
                            currentBikeIsAnEBike = bike.isElectric
                            modified = true
                        }
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
                    activityByBike.bikeName == currentBike!!.name || (activityByBike.bikeName == null && activityByBike.isEBikeSpecific == currentBikeIsAnEBike)
                }
            }
            val filteredShortRideActivities = if (currentBikeIsAnEBike) {
                shortRideActivities
            } else {
                shortRideActivities.filter { activityWithBikeData ->
                    !activityWithBikeData.isEBikeSpecific
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.activities_for_a_quick_ride)
                    )
                }

                items(
                    items = filteredShortRideActivities,
                    key = { "temp-${it.activityUid}" }) { activity ->
                    TransientTodoListItem(
                        activity = activity,
                        onCheckboxCallback = { checked ->
                            prepareQuickRideViewModel.updateRideActivity(
                                activityUid = activity.activityUid,
                                isCompleted = checked
                            )
                        },
                        suppressDueDate = true
                    )
                }

                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.activities_that_are_due_anyway)
                    )
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
                        componentUid = activityByBike.activityComponentUid,
                        uid = activityByBike.activityUid
                    )
                    TodoListItem(
                        activity = activityByBike,
                        destinationsNavigator = destinationsNavigator,
                        onCheckboxCallback = { result ->
                            prepareQuickRideViewModel.updateActivity(
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
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    prepareQuickRideViewModel.endCurrentRideAndStartFromScratch()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.btn_desc_clear_and_start_anew)
                    )
                    IconSpacer()
                    Text(text = stringResource(R.string.btn_text_start_new_quick_ride))
                }
            }
        }
    }
}

fun pxToDp(pixels: Float, density: Density): Dp {
    val dp = with(density) { pixels.toDp() }
    return dp
}
