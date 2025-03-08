package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ActivityDeleteViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.ShowActivityDetails
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActivityDeleteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ActivityDelete(
    activityUid: Long,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    val activityDeleteViewModel =
        hiltViewModel<ActivityDeleteViewModel, ActivityDeleteViewModel.ActivityDeleteViewModelFactory> { factory ->
            factory.create(activityUid)
        }

    KJsResponsiveNavigation(
        ActivityDeleteDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_text_cancel),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.btn_text_delete),
            onClick = {
                activityDeleteViewModel.commitDelete()
                destinationsNavigator.navigateUp()
            }
        ),
        headline = stringResource(R.string.hdr_delete_an_activity)
    ) {
        val activity by activityDeleteViewModel.activity.observeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (activity != null) {
                Text(text = "You are about to delete activity ${activity?.title}!")
                DefaultSpacer()
                ShowActivityDetails(activity)
            } else {
                Text(text = stringResource(R.string.we_can_not_find_this_activity))
            }
        }
    }
}