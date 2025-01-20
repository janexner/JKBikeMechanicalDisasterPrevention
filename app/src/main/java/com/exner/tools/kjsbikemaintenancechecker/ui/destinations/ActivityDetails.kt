package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ActivityDetails(
    activityUuid: Long,
    destinationsNavigator: DestinationsNavigator
) {}