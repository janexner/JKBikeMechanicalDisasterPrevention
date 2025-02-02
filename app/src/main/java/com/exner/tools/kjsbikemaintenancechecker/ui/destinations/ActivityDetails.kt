package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.R
import com.exner.tools.kjsbikemaintenancechecker.ui.ActivityDetailsViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.ShowActivityDetails
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.ActivityEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ActivityDetails(
    activityUid: Long,
    destinationsNavigator: DestinationsNavigator
) {

    val activityDetailsViewModel =
        hiltViewModel<ActivityDetailsViewModel, ActivityDetailsViewModel.ActivityDetailsViewModelFactory> { factory ->
            factory.create(activityUid = activityUid)
        }

    val activity by activityDetailsViewModel.activity.observeAsState()

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                if (activity != null) {
                    ShowActivityDetails(activity)
                } else {
                    Text(text = stringResource(R.string.we_can_not_find_this_activity))
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        destinationsNavigator.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                    IconSpacer()
                    IconButton(onClick = {
                         destinationsNavigator.navigate(ActivityDeleteDestination(activityUid = activityUid))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
                floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text(text = stringResource(R.string.edit)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = stringResource(R.string.edit_the_activity)
                                )
                            },
                            onClick = {
                                destinationsNavigator.navigate(ActivityEditDestination(activityUid))
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                }
            )
        }
    )
}
