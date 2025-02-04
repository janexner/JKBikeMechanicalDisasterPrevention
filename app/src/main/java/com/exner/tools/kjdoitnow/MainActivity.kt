package com.exner.tools.kjdoitnow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.exner.tools.kjdoitnow.database.tools.ArchiveDataWorker
import com.exner.tools.kjdoitnow.preferences.UserPreferencesManager
import com.exner.tools.kjdoitnow.state.ThemeStateHolder
import com.exner.tools.kjdoitnow.ui.KJsGlobalScaffold
import com.exner.tools.kjdoitnow.ui.theme.KJsBikeMaintenanceCheckerTheme
import com.exner.tools.kjdoitnow.ui.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
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

            // launch the periodic archiving job
            val context = LocalContext.current
            val constraints =
                Constraints.Builder().setRequiresBatteryNotLow(requiresBatteryNotLow = true).build()
            val archiveDataWorker =
                OneTimeWorkRequestBuilder<ArchiveDataWorker>().setConstraints(constraints)
                    .setInitialDelay(30, TimeUnit.SECONDS).build()
            WorkManager.getInstance(context).enqueue(archiveDataWorker)
            WorkManager.getInstance(context).getWorkInfoByIdLiveData(archiveDataWorker.id)
                .observe(this) { workInfo ->
                    if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(context,
                            getString(R.string.toast_archive_job_successful), Toast.LENGTH_LONG).show()
                    }
                }

            KJsBikeMaintenanceCheckerTheme(
                darkTheme = userTheme.value.userSelectedTheme == Theme.Dark || (userTheme.value.userSelectedTheme == Theme.Auto && isSystemInDarkTheme())
            ) {
                KJsGlobalScaffold(windowSizeClass)
            }
        }
    }
}
