package com.exner.tools.jkbikemechanicaldisasterprevention.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsMenuItem
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.NavigationStyle
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareBikeHolidaysDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareDayOutDestination
import com.ramcosta.composedestinations.generated.destinations.PrepareQuickRideDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.BaseRoute

@Composable
fun KJsResponsiveNavigation(
    currentDestination: BaseRoute,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass,
    content: @Composable () -> Unit
) {
    val listOfMenuItems: List<KJsMenuItem> = listOf(
        KJsMenuItem(
            label = stringResource(R.string.tab_home),
            icon = Icons.Default.Home,
            selected = HomeDestination == currentDestination,
            onClick = {
                destinationsNavigator.navigate(HomeDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_quick_ride),
            icon = Icons.Default.ThumbUp,
            selected = PrepareQuickRideDestination == currentDestination,
            onClick = {
                destinationsNavigator.navigate(PrepareQuickRideDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_day_out),
            icon = Icons.Default.Hail,
            selected = PrepareDayOutDestination == currentDestination,
            onClick = {
                destinationsNavigator.navigate(PrepareDayOutDestination)
            }
        ),
        KJsMenuItem(
            label = stringResource(R.string.tab_holidays),
            icon = Icons.Default.Luggage,
            selected = PrepareBikeHolidaysDestination == currentDestination,
            onClick = {
                destinationsNavigator.navigate(PrepareBikeHolidaysDestination)
            }
        ),
    )

    val navigationStyle =
        NavigationStyle.getNavigationStyleForWidthSizeClass(windowSizeClass.widthSizeClass)

    when (navigationStyle) {
        NavigationStyle.BOTTOM_BAR -> {
            Scaffold(
                modifier = Modifier
                    .imePadding(),
                content = { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        content()
                    }
                },
                bottomBar = {
                    if (currentDestination == HomeDestination || currentDestination == PrepareDayOutDestination || currentDestination == PrepareQuickRideDestination || currentDestination == PrepareBikeHolidaysDestination) {
                        NavigationBar {
                            listOfMenuItems.forEach { item ->
                                NavigationBarItem(
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
                        }
                    }
                }
            )
        }

        NavigationStyle.LEFT_RAIL -> {
            Row(
                modifier = Modifier
                    .imePadding()
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
                        selected = SettingsDestination == currentDestination,
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
                Box(
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = 0.dp,
                        end = 0.dp,
                        bottom = 32.dp
                    )
                ) {
                    content()
                }
            }
        }

        NavigationStyle.LEFT_DRAWER -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
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
                                    selected = SettingsDestination == currentDestination,
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 32.dp)

                    ) {
                        content()
                    }
                }
            }
        }
    }
}