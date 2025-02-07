package com.exner.tools.jkbikemechanicaldisasterprevention.state

import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.Theme
import kotlinx.coroutines.flow.StateFlow

interface ThemeStateHolder {
    val themeState: StateFlow<ThemeState>
    fun updateTheme(theme: Theme)
}