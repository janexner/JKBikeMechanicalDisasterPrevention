package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ExportDataViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.RideLevel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ExportDataDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ExportData(
    exportDataViewModel: ExportDataViewModel = hiltViewModel(),
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_export_data))

    val context = LocalContext.current

    KJsResponsiveNavigation(
        ExportDataDestination,
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
            imageVector = Icons.Default.ImportExport,
            contentDescription = stringResource(R.string.btn_text_export_data),
            onClick = {
                exportDataViewModel.commitExport(
                    context = context,
                    successCallback = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.msg_data_exported_to_downloads_folder),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        ),
        headline = stringResource(R.string.hdr_export_data)
    ) {
        val bikes by exportDataViewModel.allBikes.collectAsState(
            emptyList()
        )
        val activities by exportDataViewModel.allActivities.collectAsState(
            emptyList()
        )
        val templates by exportDataViewModel.allTemplates.collectAsState(
            emptyList()
        )

        val components by exportDataViewModel.allComponents.collectAsState(
            emptyList()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.data_that_will_be_exported))
            DefaultSpacer()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_bikes),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                items(items = bikes, key = { "bike-${it.uid}" }) {
                    Text(text = it.name)
                }

                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_activities),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                items(items = activities, key = { "activity-${it.uid}" }) {
                    Text(text = it.title)
                }

                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_template_activities),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                items(items = templates, key = { "template-${it.uid}" }) {
                    Text(
                        text = "${
                            RideLevel.getLabel(
                                context,
                                it.rideLevel
                            )
                        } - ${it.title}"
                    )
                }

                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_components),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                items(items = components, key = { "component-${it.uid}" }) {
                    Text(text = it.name)
                }
            }
        }
    }
}
