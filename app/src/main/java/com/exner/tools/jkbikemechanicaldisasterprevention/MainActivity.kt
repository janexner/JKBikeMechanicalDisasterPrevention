package com.exner.tools.jkbikemechanicaldisasterprevention

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import com.exner.tools.jkbikemechanicaldisasterprevention.preferences.UserPreferencesManager
import com.exner.tools.jkbikemechanicaldisasterprevention.state.ThemeStateHolder
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffold
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.KJsBikeMaintenanceCheckerTheme
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager // TODO

    @Inject
    lateinit var themeStateHolder: ThemeStateHolder

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            // night mode has two possible triggers:
            // - device may be in night mode
            // - force night mode setting may be on
            val userTheme = themeStateHolder.themeState.collectAsState()

            // window size class
            val windowSizeClass = calculateWindowSizeClass(this)

            KJsBikeMaintenanceCheckerTheme(
                darkTheme = userTheme.value.userSelectedTheme == Theme.Dark || (userTheme.value.userSelectedTheme == Theme.Auto && isSystemInDarkTheme())
            ) {
                KJsGlobalScaffold(windowSizeClass)
            }
        }
    }
}
