package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.outlined.Hail
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.exner.tools.kjsbikemaintenancechecker.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareShortRideDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun PrepareBikeHolidays(
    destinationsNavigator: DestinationsNavigator
) {

    Scaffold(
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                stickyHeader {
                    Text(text = "TODOs for a bike holiday:")
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(HomeDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.Home, contentDescription = stringResource(R.string.tab_home))
                    },
                    label = { Text(text = stringResource(R.string.tab_home)) },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareShortRideDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.ThumbUp, contentDescription = stringResource(R.string.tab_quick_ride))
                    },
                    label = { Text(text = stringResource(R.string.tab_quick_ride)) },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareDayOutDestination)
                    },
                    icon = {
                        Icon(Icons.Outlined.Hail, contentDescription = stringResource(R.string.tab_day_out))
                    },
                    label = { Text(text = stringResource(R.string.tab_day_out)) },
                    selected = false
                )
                NavigationBarItem(
                    onClick = {
                        destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
                    },
                    icon = {
                        Icon(Icons.Filled.Luggage, contentDescription = stringResource(R.string.tab_holidays))
                    },
                    label = { Text(text = stringResource(R.string.tab_holidays)) },
                    selected = true
                )
            }
        }
    )
}
