package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.ShowActivityDetails
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
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        ActivityDetailsDestination,
        destinationsNavigator,
        windowSizeClass
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
            PageHeaderTextWithSpacer(stringResource(R.string.activity_details))
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
            Spacer(modifier = Modifier.weight(0.7f))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
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
                Spacer(modifier = Modifier.weight(0.7f))
                Button(
                    onClick = {
                        destinationsNavigator.navigate(ActivityEditDestination(activityUid))
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(R.string.edit_the_activity)
                    )
                    Text(text = stringResource(R.string.edit))
                }
            }
        }
    }
}
