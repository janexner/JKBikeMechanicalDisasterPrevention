package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ExportData(
    exportDataViewModel: ExportDataViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val bikes by exportDataViewModel.allBikes.collectAsState(
        emptyList()
    )
    val activities by exportDataViewModel.allActivities.collectAsState(
        emptyList()
    )
    val templates by exportDataViewModel.allTemplates.collectAsState(
        emptyList()
    )

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.data_that_will_be_exported))
                DefaultSpacer()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
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
                        Text(text = "${it.rideLevel?.name} - ${it.title}")
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(R.string.export)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = stringResource(R.string.export)
                            )
                        },
                        onClick = {
                            exportDataViewModel.commitExport(
                                context = context,
                                successCallback = {
                                    Toast.makeText(
                                        context,
                                        "Data exported to Downloads folder!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
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
