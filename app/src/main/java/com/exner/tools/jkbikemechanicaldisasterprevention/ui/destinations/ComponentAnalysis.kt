package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.views.RetiredComponents
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ComponentAnalysisViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ManageComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun ComponentAnalysis(
    componentAnalysisViewModel: ComponentAnalysisViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        ManageComponentsDestination,
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
        headline = stringResource(R.string.hdr_analyse_components)
    ) {
        val retiredComponents: List<RetiredComponents> by componentAnalysisViewModel.retiredComponents.collectAsState(
            emptyList()
        )
        val listOfComponentUidsForAnalysis by componentAnalysisViewModel.listOfComponentUidsForAnalysis.collectAsState(
            emptyList()
        )

        val analysisResults by componentAnalysisViewModel.componentAnalysisResults.collectAsState(
            null
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (retiredComponents.isNotEmpty()) {
                Text(text = stringResource(R.string.select_components_for_analysis))
                DefaultSpacer()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                ) {
                    items(retiredComponents, key = { it.uid }) { retiredComponent ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = listOfComponentUidsForAnalysis.contains(retiredComponent.uid),
                                onCheckedChange = {
                                    if (it) {
                                        componentAnalysisViewModel.addUidToList(retiredComponent.uid)
                                    } else {
                                        componentAnalysisViewModel.removeUidFromList(
                                            retiredComponent.uid
                                        )
                                    }
                                }
                            )
                            Text(text = retiredComponent.name + " / " + retiredComponent.description)
                        }
                    }
                }
                DefaultSpacer()
                Button(onClick = {
                    componentAnalysisViewModel.runAnalysis()
                }) {
                    Text(text = stringResource(R.string.btn_text_analyse))
                }
                DefaultSpacer()
                if (analysisResults != null) {
                    Text(text = "Results:")
                    DefaultSpacer()
                    Text(text = "Total mileage ${analysisResults!!.totalUsageMiles}")
                    DefaultSpacer()
                    Text(text = "Total days in use ${analysisResults!!.totalUsageDays}")
                    DefaultSpacer()
                }
            } else { // there are no retired components to analyse
                Text(text = stringResource(R.string.there_are_no_retired_components_to_analyse))
            }
        }
    }
}
