package com.exner.tools.kjsbikemaintenancechecker.state

import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme
import kotlinx.coroutines.flow.StateFlow

interface ThemeStateHolder {
    val themeState: StateFlow<ThemeState>
    fun updateTheme(theme: Theme)
}