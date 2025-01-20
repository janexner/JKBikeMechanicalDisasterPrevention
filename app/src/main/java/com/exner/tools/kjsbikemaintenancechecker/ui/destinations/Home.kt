package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
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

    Text(text = "Home Screen")
}