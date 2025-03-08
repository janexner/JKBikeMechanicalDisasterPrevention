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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.BikeDeleteViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.ManageBikesDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun BikeDelete(
    bikeUid: Long,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    val bikeDeleteViewModel =
        hiltViewModel<BikeDeleteViewModel, BikeDeleteViewModel.BikeDeleteViewModelFactory> { factory ->
            factory.create(bikeUid = bikeUid)
        }

    KJsResponsiveNavigation(
        BikeDeleteDestination,
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
                bikeDeleteViewModel.commitDelete()
                destinationsNavigator.popBackStack(
                    ManageBikesDestination,
                    inclusive = false
                )
            }
        ),
        headline = stringResource(R.string.hdr_delete_a_bike)
    ) {
        val bike by bikeDeleteViewModel.bike.observeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (bike != null) {
                Text(text = "You are about to delete the bike ${bike!!.name}.")
            } else {
                Text(text = stringResource(R.string.we_can_not_find_this_bike))
            }
        }
    }
}
