package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
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
import com.exner.tools.kjsbikemaintenancechecker.ui.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.WelcomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>(start = true)
@Composable
fun Welcome(
    welcomeViewModel: WelcomeViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {
    val showOnboarding = welcomeViewModel.needsOnboarding.collectAsState()

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding).padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Welcome to KJ's Bike Maintenance Checker!")
                DefaultSpacer()
                if (showOnboarding.value) {
                    // TODO
                } else {
                    // everyday UI
                    Button(onClick = { destinationsNavigator.navigate(HomeDestination()) }) {
                        Text(text = "Move to the Home Screen")
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Add") },
                        icon = {
                            Icon(Icons.Default.AddCircle, "Add a bike or components")
                        },
                        onClick = {
                            // destinationsNavigator.navigate(HomeDestination())
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            )
        }
    )
}