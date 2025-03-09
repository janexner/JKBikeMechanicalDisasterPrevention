package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ActivityDetailsViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.ShowActivityDetails
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.ActivityDetailsDestination
import com.ramcosta.composedestinations.generated.destinations.ActivityEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ActivityDetails(
    activityUid: Long,
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_activity_details))

    KJsResponsiveNavigation(
        ActivityDetailsDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_desc_back),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            ),
            KJsAction(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.btn_text_delete),
                onClick = {
                    destinationsNavigator.navigate(ActivityDeleteDestination(activityUid = activityUid))
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.btn_text_edit),
            onClick = {
                destinationsNavigator.navigate(ActivityEditDestination(activityUid))
            }
        ),
        headline = stringResource(R.string.hdr_activity_details)
    ) {
        val activityDetailsViewModel =
            hiltViewModel<ActivityDetailsViewModel, ActivityDetailsViewModel.ActivityDetailsViewModelFactory> { factory ->
                factory.create(activityUid = activityUid)
            }

        val activity by activityDetailsViewModel.activity.observeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                if (activity != null) {
                    ShowActivityDetails(activity)
                } else {
                    Text(text = stringResource(R.string.we_can_not_find_this_activity))
                }
            }
        }
    }
}
