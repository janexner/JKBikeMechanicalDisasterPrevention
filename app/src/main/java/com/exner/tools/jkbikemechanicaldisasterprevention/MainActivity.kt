package com.exner.tools.jkbikemechanicaldisasterprevention

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.exner.tools.jkbikemechanicaldisasterprevention.preferences.UserPreferencesManager
import com.exner.tools.jkbikemechanicaldisasterprevention.scheduler.CheckComponentsIntervalsAndCreateActivitiesWorker
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffold
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.MainViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.KJsBikeMaintenanceCheckerTheme
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager

    private val viewModel: MainViewModel by viewModels()

    // launch the scheduler unless it is running already
    private val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiresStorageNotLow(true)
        .build()
    private val schedulerWorkRequest: PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<CheckComponentsIntervalsAndCreateActivitiesWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS,
            flexTimeInterval = 13,
            flexTimeIntervalUnit = TimeUnit.HOURS
        ).setConstraints(constraints).build()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workManager = WorkManager.getInstance(this)
        // see whether there is a running scheduler
        // if not, launch one
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = "CheckComponentsIntervalsAndCreateActivities",
            request = schedulerWorkRequest,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE
        )

        enableEdgeToEdge()
        setContent {
            // night mode has two possible triggers:
            // - device may be in night mode
            // - force night mode setting may be on
            val userTheme = viewModel.userSelectedTheme.collectAsState()

            // window size class
            val windowSizeClass = calculateWindowSizeClass(this)

            KJsBikeMaintenanceCheckerTheme(
                darkTheme = userTheme.value == Theme.Dark || (userTheme.value == Theme.Auto && isSystemInDarkTheme())
            ) {
                KJsGlobalScaffold(windowSizeClass)
            }
        }
    }
}
