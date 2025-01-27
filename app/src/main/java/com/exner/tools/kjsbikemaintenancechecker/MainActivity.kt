package com.exner.tools.kjsbikemaintenancechecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import com.exner.tools.kjsbikemaintenancechecker.state.ThemeStateHolder
import com.exner.tools.kjsbikemaintenancechecker.ui.KJsGlobalScaffold
import com.exner.tools.kjsbikemaintenancechecker.ui.theme.KJsBikeMaintenanceCheckerTheme
import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager // TODO
    @Inject
    lateinit var themeStateHolder: ThemeStateHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // night mode has two possible triggers:
            // - device may be in night mode
            // - force night mode setting may be on
            val userTheme = themeStateHolder.themeState.collectAsState()

            KJsBikeMaintenanceCheckerTheme(
                darkTheme = userTheme.value.userSelectedTheme == Theme.Dark || (userTheme.value.userSelectedTheme == Theme.Auto && isSystemInDarkTheme())
            )  {
                KJsGlobalScaffold()
            }
        }
    }
}
