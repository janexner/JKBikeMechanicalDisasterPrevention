package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.exner.tools.kjsbikemaintenancechecker.R
import com.exner.tools.kjsbikemaintenancechecker.database.entities.TemplateActivity
import com.exner.tools.kjsbikemaintenancechecker.ui.ManageTemplateActivitiesViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.RideLevelSelector
import com.exner.tools.kjsbikemaintenancechecker.ui.components.TemplateActivityListItem
import com.exner.tools.kjsbikemaintenancechecker.ui.helpers.RideLevel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityAddDestination
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun ManageTemplateActivities(
    manageTemplateActivitiesViewModel: ManageTemplateActivitiesViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val rideLevels: List<RideLevel> by manageTemplateActivitiesViewModel.rideLevel.collectAsStateWithLifecycle()
    var currentRideLevel: RideLevel? by remember { mutableStateOf(null) }

    val templateActivities by manageTemplateActivitiesViewModel.templateActivities.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                RideLevelSelector(
                    currentRideLevel,
                    rideLevels,
                    leftAlign = false
                ) {
                    currentRideLevel = it
                }
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
                DefaultSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        destinationsNavigator.navigate(TemplateActivityAddDestination)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.lbl_add_template_activity)
                        )
                        IconSpacer()
                        Text(text = stringResource(R.string.lbl_add_template_activity))
                    }
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
                }
            )
        }
    )
}
