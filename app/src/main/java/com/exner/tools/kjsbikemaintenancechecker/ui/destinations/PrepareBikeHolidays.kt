package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.runtime.Composable
import com.exner.tools.kjsbikemaintenancechecker.ui.destinations.wrappers.OnboardingWrapper
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(
    wrappers = [OnboardingWrapper::class]
)
@Composable
fun PrepareBikeHolidays() {}