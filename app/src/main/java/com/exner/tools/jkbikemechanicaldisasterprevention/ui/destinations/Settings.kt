package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndTriStateToggle
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
        windowSizeClass
    ) {
        val userSelectedTheme by settingsViewModel.userSelectedTheme.collectAsStateWithLifecycle()
        val todoListsExpire by settingsViewModel.todoListsExpire.collectAsStateWithLifecycle()
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            PageHeaderTextWithSpacer(stringResource(R.string.menu_item_settings))
            TextAndTriStateToggle(
                text = stringResource(R.string.lbl_theme),
                currentTheme = userSelectedTheme,
                updateTheme = { it: Theme ->
                    settingsViewModel.updateUserSelectedTheme(it)
                }
            )
            DefaultSpacer()
            TextAndSwitch(
                text = "TODO lists / rides expire after 2 days",
                checked = todoListsExpire
            ) {
                settingsViewModel.updateTodoListsExpire(it)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    destinationsNavigator
                        .navigate(ManageTemplateActivitiesDestination)
                }) {
                    Icon(
                        imageVector = Icons.Default.Dataset,
                        contentDescription = stringResource(R.string.lbl_manage_template_activities)
                    )
                    IconSpacer()
                    Text(text = stringResource(R.string.lbl_manage_template_activities))
                }
            }
        }
    }
}

