package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.SettingsViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndTriStateToggle
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ManageTemplateActivitiesDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun Settings(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        SettingsDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_desc_back),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Dataset,
            contentDescription = stringResource(R.string.btn_text_manage_template_activities),
            onClick = {
                destinationsNavigator.navigate(ManageTemplateActivitiesDestination)
            }
        ),
        headline = stringResource(R.string.hdr_settings)
    ) {
        val userSelectedTheme by settingsViewModel.userSelectedTheme.collectAsStateWithLifecycle()
        val todoListsExpire by settingsViewModel.todoListsExpire.collectAsStateWithLifecycle()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            TextAndTriStateToggle(
                text = stringResource(R.string.lbl_theme),
                currentTheme = userSelectedTheme,
                updateTheme = { it: Theme ->
                    settingsViewModel.updateUserSelectedTheme(it)
                }
            )
            DefaultSpacer()
            TextAndSwitch(
                text = stringResource(R.string.lbl_todo_lists_rides_expire_after_2_days),
                checked = todoListsExpire
            ) {
                settingsViewModel.updateTodoListsExpire(it)
            }
        }
    }
}

