package com.exner.tools.kjdoitnow.state

import com.exner.tools.kjdoitnow.ui.theme.Theme
import kotlinx.coroutines.flow.StateFlow

interface ThemeStateHolder {
    val themeState: StateFlow<ThemeState>
    fun updateTheme(theme: Theme)
}