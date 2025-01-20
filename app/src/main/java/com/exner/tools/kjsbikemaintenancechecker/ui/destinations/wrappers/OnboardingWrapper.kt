package com.exner.tools.kjsbikemaintenancechecker.ui.destinations.wrappers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.WelcomeViewModel
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.wrapper.DestinationWrapper

object OnboardingWrapper : DestinationWrapper {

    @Composable
    override fun <T> DestinationScope<T>.Wrap(screenContent: @Composable () -> Unit) {

        val welcomeViewModel: WelcomeViewModel = hiltViewModel()
        val needsOnboarding by welcomeViewModel.needsOnboarding.collectAsState()

        if (needsOnboarding) {
            // TODO
        } else {
            screenContent()
        }
    }

}
