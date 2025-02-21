package com.exner.tools.jkbikemechanicaldisasterprevention

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KJsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // let's try dynamic colours
        DynamicColors.applyToActivitiesIfAvailable(this)

    }
}