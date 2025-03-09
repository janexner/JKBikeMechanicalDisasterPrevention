package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.NavigationStyle
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AboutDestination
import com.ramcosta.composedestinations.generated.destinations.AutomaticActivitiesGenerationLogListDestination
import com.ramcosta.composedestinations.generated.destinations.BikeAddDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentAddDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentAnalysisDestination
import com.ramcosta.composedestinations.generated.destinations.ExportDataDestination
import com.ramcosta.composedestinations.generated.destinations.HomeDestination
import com.ramcosta.composedestinations.generated.destinations.ImportDataDestination
import com.ramcosta.composedestinations.generated.destinations.ManageBikesDestination
import com.ramcosta.composedestinations.generated.destinations.ManageComponentsDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsDestination
import com.ramcosta.composedestinations.generated.destinations.StravaAuthResultDeepLinkTargetDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator

@Composable
fun KJsGlobalScaffold(
    activity: ComponentActivity,
    windowSizeClass: WindowSizeClass,
    intent: Intent,
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel = hiltViewModel(),
) {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()
    val destinationsNavigator = navController.rememberDestinationsNavigator()
    val destination = navController.currentDestinationAsState().value

    val navigationStyle =
        NavigationStyle.getNavigationStyleForWidthSizeClass(windowSizeClass.widthSizeClass)

    val destinationTitle by kJsGlobalScaffoldViewModel.destinationTitle.collectAsStateWithLifecycle()

    val numberOfRetiredComponents by kJsGlobalScaffoldViewModel.numberOfRetiredComponents.collectAsStateWithLifecycle(
        0
    )

    Scaffold(
        topBar = {
            KJsTopBar(
                destination,
                destinationsNavigator,
                navigationStyle,
                destinationTitle,
                numberOfRetiredComponents
            )
        },
        content = { innerPadding ->
            val newPadding = PaddingValues.Absolute(
                innerPadding.calculateLeftPadding(LayoutDirection.Ltr),
                innerPadding.calculateTopPadding(),
                innerPadding.calculateRightPadding(LayoutDirection.Ltr),
                0.dp
            )
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.root,
                dependenciesContainerBuilder = { //this: DependenciesContainerBuilder<*>
                    dependency(windowSizeClass)
                    // make this ViewModel available to all destinations, and scoped on the Activity
                    dependency(hiltViewModel<KJsGlobalScaffoldViewModel>(activity))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(newPadding)
                    .padding(newPadding)
            ) {
            }
            // how about that intent?
            if (intent.action == "android.intent.action.VIEW" && intent.data?.host == "jkbike.net") {
                val code : String? = intent.data?.getQueryParameter("code")
                val scope: String? = intent.data?.getQueryParameter("scope")
                if (!code.isNullOrBlank() && !scope.isNullOrBlank()) {
                    destinationsNavigator.navigate(
                        StravaAuthResultDeepLinkTargetDestination(
                            code,
                            scope
                        )
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KJsTopBar(
    destination: DestinationSpec?,
    destinationsNavigator: DestinationsNavigator,
    navigationStyle: NavigationStyle,
    destinationTitle: String,
    numberOfRetiredComponents: Int
) {

    when (navigationStyle) {
        NavigationStyle.BOTTOM_BAR -> { // compact
            TopAppBar(
                title = { Text(text = stringResource(R.string.top_bar_title)) },
                navigationIcon = {
                    when (destination) {
                        HomeDestination -> {
                            // no back button here
                        }

                        else -> {
                            IconButton(onClick = { destinationsNavigator.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.btn_desc_back)
                                )
                            }
                        }
                    }
                },
                actions = {
                    MainMenuAction(destination, destinationsNavigator, numberOfRetiredComponents)
                }
            )
        }

        NavigationStyle.LEFT_RAIL -> { // medium
            val title = if (destinationTitle.isNotBlank()) {
                stringResource(R.string.top_bar_title) + " - " + destinationTitle
            } else {
                stringResource(R.string.top_bar_title_long)
            }
            CenterAlignedTopAppBar(
                title = {
                    Text(text = title)
                },
                actions = {
                    MainMenuAction(destination, destinationsNavigator, numberOfRetiredComponents)
                },
            )
        }

        NavigationStyle.LEFT_DRAWER -> { // expanded
            val title =
                stringResource(R.string.top_bar_title_long) + if (destinationTitle.isNotBlank()) {
                    " - $destinationTitle"
                } else {
                    ""
                }
            CenterAlignedTopAppBar(
                title = {
                    Text(text = title)
                },
                actions = {
                    MainMenuAction(destination, destinationsNavigator, numberOfRetiredComponents)
                },
            )
        }
    }
}

@Composable
private fun MainMenuAction(
    destination: DestinationSpec?,
    destinationsNavigator: DestinationsNavigator,
    numberOfRetiredComponents: Int
) {

    var displayMainMenu by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            displayMainMenu = !displayMainMenu
        }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.menu)
        )
    }
    DropdownMenu(
        expanded = displayMainMenu,
        onDismissRequest = { displayMainMenu = false }
    ) {
        DropdownMenuItem(
            enabled = destination != BikeAddDestination,
            text = {
                Text(
                    text = stringResource(R.string.menu_item_add_bike),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(BikeAddDestination)
            }
        )
        DropdownMenuItem(
            enabled = destination != ManageBikesDestination,
            text = {
                Text(
                    text = stringResource(R.string.menu_item_manage_bikes_components),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(ManageBikesDestination)
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            enabled = destination != ComponentAddDestination,
            text = {
                Text(
                    text = stringResource(R.string.menu_item_add_component),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(ComponentAddDestination)
            }
        )
        DropdownMenuItem(
            enabled = destination != ManageComponentsDestination,
            text = {
                Text(
                    text = stringResource(R.string.menu_item_manage_components),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(ManageComponentsDestination)
            }
        )
        if (numberOfRetiredComponents > 0) {
            DropdownMenuItem(
                enabled = destination != ComponentAnalysisDestination,
                text = {
                    Text(
                        text = stringResource(R.string.menu_item_analyse_components),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = {
                    displayMainMenu = false
                    destinationsNavigator.navigate(ComponentAnalysisDestination)
                }
            )
        }
        HorizontalDivider()
        DropdownMenuItem(
            enabled = destination != SettingsDestination,
            text = {
                Text(
                    text = stringResource(R.string.menu_item_settings),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(SettingsDestination)
            }
        )
        DropdownMenuItem(
            enabled = destination != ImportDataDestination,
            text = {
                Text(
                    text = stringResource(R.string.btn_text_import_data),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(ImportDataDestination)
            }
        )
        DropdownMenuItem(
            enabled = destination != ExportDataDestination,
            text = {
                Text(
                    text = stringResource(R.string.btn_text_export_data),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(ExportDataDestination)
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            enabled = destination != AutomaticActivitiesGenerationLogListDestination,
            text = {
                Text(
                    text = stringResource(R.string.menu_item_log_list),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(AutomaticActivitiesGenerationLogListDestination)
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            enabled = destination != AboutDestination,
            text = {
                Text(
                    text = stringResource(R.string.menu_item_about),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            onClick = {
                displayMainMenu = false
                destinationsNavigator.navigate(AboutDestination)
            }
        )
    }
}