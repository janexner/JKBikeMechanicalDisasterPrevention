package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ManageBikesViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BikeEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ManageBikes(
    manageBikesViewModel: ManageBikesViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val bikes: List<Bike> by manageBikesViewModel.bikes.collectAsState(
        initial = emptyList()
    )

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                PageHeaderTextWithSpacer(stringResource(R.string.manage_bikes_components))
                Text(text = stringResource(R.string.tap_bike_to_edit))
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

                    items(bikes, key = { "bike.${it.uid}" }) { bike ->
                        Surface(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        destinationsNavigator.navigate(BikeEditDestination(bike.uid))
                                    },
                                )
                                .padding(4.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                                    contentDescription = stringResource(R.string.bike),
                                )
                                IconSpacer()
                                Text(
                                    text = bike.name,
                                    modifier = Modifier.weight(1f)
                                )
                                DefaultSpacer()
                                Text(
                                    text = "${bike.mileage} km",
                                )
                                DefaultSpacer()
                                Text(
                                    text = if (bike.lastUsedDate != null) {
                                        bike.lastUsedDate.toString()
                                    } else {
                                        stringResource(R.string.not_yet_used)
                                    },
                                )
                            }
                        }
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
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                floatingActionButton = {
                }
            )
        }
    )
}