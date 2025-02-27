package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ComponentDeleteViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ComponentDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.ManageComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ComponentDelete(
    componentUid: Long,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        ComponentDeleteDestination,
        destinationsNavigator,
        windowSizeClass
    ) {
        val componentDeleteViewModel =
            hiltViewModel<ComponentDeleteViewModel, ComponentDeleteViewModel.ComponentDeleteViewModelFactory> { factory ->
                factory.create(componentUid = componentUid)
            }

        val component by componentDeleteViewModel.component.observeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            PageHeaderTextWithSpacer(stringResource(R.string.delete_a_component))
            if (component != null) {
                Text(text = "You are about to delete the component ${component!!.name}.")
            } else {
                Text(text = stringResource(R.string.we_can_not_find_this_component))
            }
            Spacer(modifier = Modifier.weight(0.7f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    destinationsNavigator.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.cancel)
                    )
                }
                Button(
                    onClick = {
                        componentDeleteViewModel.commitDelete()
                        destinationsNavigator.popBackStack(
                            ManageComponentsDestination,
                            inclusive = false
                        )
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(R.string.delete_the_component)
                    )
                    Text(text = stringResource(R.string.delete))
                }
            }
        }

    }
}