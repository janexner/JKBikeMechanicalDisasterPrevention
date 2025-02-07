package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.TemplateActivityDeleteViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.ShowTemplateActivityDetails
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ManageTemplateActivitiesDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun TemplateActivityDelete(
    templateActivityUid: Long,
    destinationsNavigator: DestinationsNavigator
) {

    val templateActivityDeleteViewModel =
        hiltViewModel<TemplateActivityDeleteViewModel, TemplateActivityDeleteViewModel.TemplateActivityDeleteViewModelFactory> { factory ->
            factory.create(templateActivityUid)
        }

    val activity by templateActivityDeleteViewModel.activity.observeAsState()

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
                PageHeaderTextWithSpacer(stringResource(R.string.delete_template_activity))
                if (activity != null) {
                    Text(text = "You are about to delete template activity ${activity?.title}!")
                    DefaultSpacer()
                    ShowTemplateActivityDetails(activity)
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
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(R.string.delete)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.delete_the_activity)
                            )
                        },
                        onClick = {
                            templateActivityDeleteViewModel.commitDelete()
                            destinationsNavigator.popBackStack(
                                ManageTemplateActivitiesDestination,
                                inclusive = false
                            )
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            )
        }
    )
}