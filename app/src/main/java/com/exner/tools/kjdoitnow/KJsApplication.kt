package com.exner.tools.kjdoitnow

import android.app.Application
import com.exner.tools.kjdoitnow.preferences.UserPreferencesManager
import com.exner.tools.kjdoitnow.state.ThemeStateHolder
import com.exner.tools.kjdoitnow.ui.theme.Theme
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class KJsApplication : Application() {
    @Inject
    lateinit var themeStateHolder: ThemeStateHolder

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate() {
        super.onCreate()

        // let's try dynamic colours
        DynamicColors.applyToActivitiesIfAvailable(this)

        // let's populate state holders from preferences where needed
        runBlocking {
            val userSelectedTheme = userPreferencesManager.theme().firstOrNull() ?: Theme.Auto
            themeStateHolder.updateTheme(userSelectedTheme)
        }
    }
}