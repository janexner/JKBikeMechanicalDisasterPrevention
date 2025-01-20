package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    val needsOnboarding: StateFlow<Boolean> = userPreferencesManager.needsOnboarding().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = true
    )

    val userSelectedTheme: StateFlow<Theme> = userPreferencesManager.theme().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = Theme.Auto
    )
}
