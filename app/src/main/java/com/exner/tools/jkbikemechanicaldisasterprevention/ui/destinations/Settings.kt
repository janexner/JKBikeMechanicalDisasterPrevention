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
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsMenuItem
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.ManageTemplateActivitiesDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareQuickRideDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun Settings(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
) {

    val userSelectedTheme by settingsViewModel.userSelectedTheme.collectAsStateWithLifecycle()
    val todoListsExpire by settingsViewModel.todoListsExpire.collectAsStateWithLifecycle()

    val listOfMenuItems: List<KJsMenuItem> = listOf(
        KJsMenuItem(
            label = stringResource(R.string.tab_home),
            icon = Icons.Default.Home,
            selected = false,
            onClick = {
                destinationsNavigator.navigate(HomeDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_quick_ride),
            icon = Icons.Default.ThumbUp,
            selected = false,
            onClick = {
                destinationsNavigator.navigate(PrepareQuickRideDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_day_out),
            icon = Icons.Default.Hail,
            selected = false,
            onClick = {
                destinationsNavigator.navigate(PrepareDayOutDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_holidays),
            icon = Icons.Default.Luggage,
            selected = false,
            onClick = {
                destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
            }
        ),
    )

    val navigationStyle = NavigationStyle.LEFT_DRAWER

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
                    listOfMenuItems.forEach { item ->
                        NavigationRailItem(
                            selected = item.selected,
                            onClick = item.onClick,
                            label = {
                                Text(text = item.label)
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.5f))
                    NavigationRailItem(
                        selected = false,
                        onClick = {
                            destinationsNavigator.navigate(SettingsDestination)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.menu_item_settings)
                            )
                        },
                        label = {
                            Text(stringResource(R.string.menu_item_settings))
                        }
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
            PermanentNavigationDrawer(
                modifier = Modifier,
                drawerContent = {
                    PermanentDrawerSheet(
                        modifier = Modifier.width(200.dp),
                        drawerContainerColor = DrawerDefaults.standardContainerColor
                    ) {
                        Column {
                            listOfMenuItems.forEach { item ->
                                NavigationDrawerItem(
                                    selected = item.selected,
                                    onClick = item.onClick,
                                    label = {
                                        Text(text = item.label)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.label
                                        )
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.weight(0.5f))
                            NavigationDrawerItem(
                                selected = true,
                                onClick = {
                                    destinationsNavigator.navigate(SettingsDestination)
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = stringResource(R.string.menu_item_settings)
                                    )
                                },
                                label = {
                                    Text(stringResource(R.string.menu_item_settings))
                                }
                            )
                        }
                    }
                },
            ) {
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
            horizontalArrangement = Arrangement.End
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