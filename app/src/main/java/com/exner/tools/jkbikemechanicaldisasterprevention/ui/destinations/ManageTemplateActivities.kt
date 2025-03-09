package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ManageTemplateActivitiesViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.RideLevelSelectorForLists
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TemplateActivityListItem
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ManageTemplateActivitiesDestination
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityAddDestination
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ManageTemplateActivities(
    manageTemplateActivitiesViewModel: ManageTemplateActivitiesViewModel = hiltViewModel(),
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_manage_template_activities))

    KJsResponsiveNavigation(
        ManageTemplateActivitiesDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_desc_back),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.btn_text_add_template_activity),
            onClick = {
                destinationsNavigator.navigate(TemplateActivityAddDestination)
            }
        ),
        headline = stringResource(R.string.hdr_manage_template_activities)
    ) {
        var currentRideLevel: Int? by remember { mutableStateOf(null) }

        val templateActivities by manageTemplateActivitiesViewModel.templateActivities.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            RideLevelSelectorForLists(
                currentRideLevel,
            ) {
                currentRideLevel = it
            }
            DefaultSpacer()
            // filter template activities by level
            val filteredTemplateActivities: List<TemplateActivity> =
                if (currentRideLevel != null) {
                    templateActivities.filter { templateActivity ->
                        templateActivity.rideLevel == currentRideLevel
                    }
                } else {
                    templateActivities
                }
            // lazyColumn those template activities
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                items(filteredTemplateActivities, key = { it.uid }) { templateActivity ->
                    TemplateActivityListItem(templateActivity) {
                        destinationsNavigator.navigate(
                            TemplateActivityEditDestination(
                                templateActivity.uid
                            )
                        )
                    }
                }
            }
        }
    }
}
