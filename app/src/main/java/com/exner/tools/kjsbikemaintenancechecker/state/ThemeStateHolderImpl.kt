package com.exner.tools.kjsbikemaintenancechecker.state

import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ThemeStateHolderImpl @Inject constructor() : ThemeStateHolder {
    private val _themeState = MutableStateFlow(ThemeState())
    override val themeState: StateFlow<ThemeState> = _themeState

    override fun updateTheme(theme: Theme) {
        // atomic
        _themeState.update { current ->
            current.copy(userSelectedTheme = theme)
        }
    }
}