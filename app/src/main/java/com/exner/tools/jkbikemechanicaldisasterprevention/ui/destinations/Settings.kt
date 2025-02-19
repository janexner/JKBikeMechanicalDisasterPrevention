package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Hail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.NavigationStyle
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.SettingsViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndTriStateToggle
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ManageTemplateActivitiesDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun Settings(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
) {

    val userSelectedTheme by settingsViewModel.userSelectedTheme.collectAsStateWithLifecycle()
    val todoListsExpire by settingsViewModel.todoListsExpire.collectAsStateWithLifecycle()

    val navigationStyle = NavigationStyle.LEFT_RAIL

    when (navigationStyle) {
        NavigationStyle.BOTTOM_BAR -> {
            // show vertically
            Scaffold(
                modifier = Modifier.imePadding(),
                content = { innerPadding ->
                    SettingsContent(
                        innerPadding,
                        userSelectedTheme,
                        settingsViewModel,
                        todoListsExpire,
                        destinationsNavigator
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        actions = {
                            IconButton(onClick = {
                                destinationsNavigator.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.cancel)
                                )
                            }
                        }
                    )
                }
            )
        }

        NavigationStyle.LEFT_RAIL -> {
            Row(
                modifier = Modifier.imePadding()
            ) {
                NavigationRail(
                    containerColor = NavigationBarDefaults.containerColor
                ) {
                    NavigationRailItem(
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = stringResource(R.string.tab_home)
                            )
                        },
                        label = {
                            Text(stringResource(R.string.tab_home))
                        }
                    )
                    NavigationRailItem(
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = stringResource(R.string.tab_quick_ride)
                            )
                        },
                        label = {
                            Text(stringResource(R.string.tab_quick_ride))
                        }
                    )
                    NavigationRailItem(
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Hail,
                                contentDescription = stringResource(R.string.tab_day_out)
                            )
                        },
                        label = {
                            Text(stringResource(R.string.tab_day_out))
                        }
                    )
                    NavigationRailItem(
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Luggage,
                                contentDescription = stringResource(R.string.tab_holidays)
                            )
                        },
                        label = {
                            Text(stringResource(R.string.tab_holidays))
                        }
                    )
                    HorizontalDivider(modifier = Modifier.width(16.dp))
                    NavigationRailItem(
                        selected = true,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = stringResource(R.string.menu_item_settings)
                            )
                        },
                        enabled = true,
                        label = {
                            Text(text = stringResource(R.string.menu_item_settings))
                        },
                    )
                }
                DefaultSpacer()
                SettingsContent(
                    PaddingValues(8.dp),
                    userSelectedTheme,
                    settingsViewModel,
                    todoListsExpire,
                    destinationsNavigator
                )
            }
        }

        NavigationStyle.LEFT_DRAWER -> {
            Row(
                modifier = Modifier.imePadding()
            ) {
                NavigationRail {
                    NavigationRailItem(
                        selected = true,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.menu_item_settings)
                            )
                        },
                        enabled = true,
                        label = {
                            Text(text = stringResource(R.string.menu_item_settings))
                        },
                    )
                }
                DefaultSpacer()
                SettingsContent(
                    PaddingValues(8.dp),
                    userSelectedTheme,
                    settingsViewModel,
                    todoListsExpire,
                    destinationsNavigator
                )
            }
        }
    }
}

@Composable
private fun SettingsContent(
    innerPadding: PaddingValues,
    userSelectedTheme: Theme,
    settingsViewModel: SettingsViewModel,
    todoListsExpire: Boolean,
    destinationsNavigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier
            .consumeWindowInsets(innerPadding)
            .padding(innerPadding)
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
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                destinationsNavigator.navigate(ManageTemplateActivitiesDestination)
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