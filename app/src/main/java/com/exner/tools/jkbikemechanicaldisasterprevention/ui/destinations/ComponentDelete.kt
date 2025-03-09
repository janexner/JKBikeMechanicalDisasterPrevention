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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ComponentDeleteViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ComponentDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.ManageComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ComponentDelete(
    componentUid: Long,
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_delete_a_component))

    val componentDeleteViewModel =
        hiltViewModel<ComponentDeleteViewModel, ComponentDeleteViewModel.ComponentDeleteViewModelFactory> { factory ->
            factory.create(componentUid = componentUid)
        }

    KJsResponsiveNavigation(
        ComponentDeleteDestination,
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
                componentDeleteViewModel.commitDelete()
                destinationsNavigator.popBackStack(
                    ManageComponentsDestination,
                    inclusive = false
                )
            }
        ),
        headline = stringResource(R.string.hdr_delete_a_component)
    ) {
        val component by componentDeleteViewModel.component.observeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (component != null) {
                Text(text = "You are about to delete the component ${component!!.name}.")
            } else {
                Text(text = stringResource(R.string.we_can_not_find_this_component))
            }
        }

    }
}