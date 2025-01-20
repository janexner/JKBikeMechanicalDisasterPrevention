package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.ButtonWithIconAndText
import com.exner.tools.kjsbikemaintenancechecker.ui.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.HomeViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.destinations.wrappers.OnboardingWrapper
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>(
    wrappers = [OnboardingWrapper::class]
)
@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val showOnboarding = homeViewModel.needsOnboarding.collectAsState()

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Welcome to KJ's Bike Maintenance Checker!")
                DefaultSpacer()
                // more UI TODO
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    ButtonWithIconAndText(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Prepare for short ride",
                        onClick = {
                            // TODO
                        }
                    )
                    ButtonWithIconAndText(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Prepare for day out",
                        onClick = {
                            // TODO
                        }
                    )
                    ButtonWithIconAndText(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add a bike or component",
                        onClick = {
                            // TODO
                        }
                    )

                },
                floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Prepare for ride") },
                            icon = {
                                Icon(Icons.Default.AddCircle, "Prepare for a ride")
                            },
                            onClick = {
//                                 destinationsNavigator.navigate(HomeDestination())
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                }
            )
        }
    )
}