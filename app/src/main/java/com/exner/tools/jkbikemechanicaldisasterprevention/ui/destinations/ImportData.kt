package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportDataViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportState
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportStateConstants
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ImportData(
    importDataViewModel: ImportDataViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {
//    val context = LocalContext.current

    val importState by importDataViewModel.importStateFlow.collectAsStateWithLifecycle(
        ImportState()
    )

    val fileForImport by importDataViewModel.file.collectAsStateWithLifecycle()

    val listOfBikesInFile by importDataViewModel.listOfBikesInFile.collectAsStateWithLifecycle()
    val listOfComponentsInFile by importDataViewModel.listOfComponentsInFile.collectAsStateWithLifecycle()

    val listOfOldBikes by importDataViewModel.listOfOldBikes.collectAsStateWithLifecycle()
    val listOfNewBikes by importDataViewModel.listOfNewBikes.collectAsStateWithLifecycle()
    val listOfClashingBikes by importDataViewModel.listOfClashingBikes.collectAsStateWithLifecycle()

    val listOfOldComponents by importDataViewModel.listOfOldComponents.collectAsStateWithLifecycle()
    val listOfNewComponents by importDataViewModel.listOfNewComponents.collectAsStateWithLifecycle()
    val listOfClashingComponents by importDataViewModel.listOfClashingComponents.collectAsStateWithLifecycle()

    val override by importDataViewModel.override.collectAsStateWithLifecycle()
    val highestUidInBikeDB by importDataViewModel.highestUidInBikeDB.collectAsStateWithLifecycle()
    val highestUidInComponentDB by importDataViewModel.highestUidInComponentDB.collectAsStateWithLifecycle()

    val errorMessage by importDataViewModel.errorMessage.collectAsStateWithLifecycle()

    val launcher = rememberFilePickerLauncher(
        type = PickerType.File(
            extensions = listOf("json")
        ),
        mode = PickerMode.Single,
        title = "Pick a JSON file"
    ) { file ->
        importDataViewModel.setFile(file)
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Import data",
                    style = MaterialTheme.typography.headlineSmall
                )
                DefaultSpacer()
                when (importState.state) {
                    ImportStateConstants.IDLE -> {
                        Text(text = "Start by selecting a file to import from")
                        DefaultSpacer()
                        Button(onClick = {
                            launcher.launch()
                        }) {
                            Text(text = "Select file")
                        }
                    }

                    ImportStateConstants.ERROR -> {
                        Text(text = "Something went wrong!")
                        DefaultSpacer()
                        Text(text = "There was an error ('$errorMessage'). You could try importing a different file?")
                        Button(onClick = {
                            launcher.launch()
                        }) {
                            Text(text = "Select different file")
                        }
                    }

                    ImportStateConstants.FILE_SELECTED -> {
                        Text(text = "File selected: ${fileForImport?.name}")
                        DefaultSpacer()
                        Button(onClick = {
                            launcher.launch()
                        }) {
                            Text(text = "Select different file")
                        }
                    }

                    ImportStateConstants.FILE_ANALYSED -> {
                        Text(text = "File selected: ${fileForImport?.name}")
                        DefaultSpacer()
                        Button(onClick = {
                            launcher.launch()
                        }) {
                            Text(text = "Select different file")
                        }
                        DefaultSpacer()
                        Text(text = "File contains ${listOfBikesInFile.size} processes and ${listOfComponentsInFile.size} categories.")
                        DefaultSpacer()
                        if (override) {
                            Text(text = "Existing processes & categories will be deleted, and ${listOfBikesInFile.size}/${listOfComponentsInFile.size} will be imported.")
                            DefaultSpacer()
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f)
                            ) {
                                stickyHeader {
                                    Text(
                                        text = "Processes",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                items(items = listOfBikesInFile) { process ->
                                    Text(text = "${process.uid} - ${process.name}")
                                }
                                stickyHeader {
                                    Text(
                                        text = "Categories",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                items(items = listOfComponentsInFile) { category ->
                                    Text(text = "${category.uid} - ${category.name}")
                                }
                            }
                        } else {
                            if (listOfOldBikes.isNotEmpty() || listOfOldComponents.isNotEmpty()) {
                                Text(text = "Processes/categories that already exist in the database: ${listOfOldBikes.size}/${listOfOldComponents.size}")
                                DefaultSpacer()
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(0.5f)
                                ) {
                                    stickyHeader {
                                        Text(
                                            text = "Processes",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    items(items = listOfOldBikes) { process ->
                                        Text(text = "${process.uid} - ${process.name}")
                                    }
                                    stickyHeader {
                                        Text(
                                            text = "Categories",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    items(items = listOfOldComponents) { category ->
                                        Text(text = "${category.uid} - ${category.name}")
                                    }
                                }
                            }
                            if (listOfClashingBikes.isNotEmpty()) {
                                Text(text = "Processes that exist but are different: ${listOfClashingBikes.size}")
                                DefaultSpacer()
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(0.5f)
                                ) {
                                    stickyHeader {
                                        Text(
                                            text = "Processes",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    items(items = listOfClashingBikes) { process ->
                                        Text(text = "${process.uid} - ${process.name}")
                                    }
                                    stickyHeader {
                                        Text(
                                            text = "Categories",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    items(items = listOfClashingComponents) { category ->
                                        Text(text = "${category.uid} - ${category.name}")
                                    }
                                }
                            }
                            if (listOfNewBikes.isNotEmpty()) {
                                Text(text = "Processes that will be imported: ${listOfNewBikes.size}")
                                DefaultSpacer()
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(0.5f)
                                ) {
                                    stickyHeader {
                                        Text(
                                            text = "Processes",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    items(items = listOfNewBikes) { process ->
                                        Text(text = "${process.uid} - ${process.name}")
                                    }
                                    stickyHeader {
                                        Text(
                                            text = "Categories",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    items(items = listOfNewComponents) { category ->
                                        Text(text = "${category.uid} - ${category.name}")
                                    }
                                }
                            } else {
                                Text(
                                    modifier = Modifier.weight(0.5f),
                                    text = "Nothing to import.",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            DefaultSpacer()
                            if (listOfClashingBikes.isNotEmpty() || listOfClashingComponents.isNotEmpty()) {
                                Text(
                                    text = "Uids overlap! This will cause an error on import!",
                                    color = MaterialTheme.colorScheme.error
                                )
                                DefaultSpacer()
                                Text(text = "The safe option is to delete all existing processes before import.")
                                DefaultSpacer()
                                Text(text = "You can also edit the JSON file and start over. When doing so, change the 'uid' fields for each process, and check whether any 'gotoId' fields have to be adjusted, too.")
                                DefaultSpacer()
                                Text(text = "The highest Uids in the database are:")
                                DefaultSpacer()
                                Text(text = "Process $highestUidInBikeDB & category $highestUidInComponentDB.")
                            }
                        }
                        TextAndSwitch(
                            text = "Delete existing processes & categories before import?",
                            checked = override
                        ) {
                            importDataViewModel.setOverride(it)
                        }
                    }

                    ImportStateConstants.IMPORT_FINISHED -> {
                        Text(text = "Import successfully done.")
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        destinationsNavigator.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancel"
                        )
                    }
                },
                floatingActionButton = {
                    if (importState.state == ImportStateConstants.FILE_ANALYSED
                        && ((listOfClashingBikes.isEmpty() && listOfClashingComponents.isEmpty()) || override)
                        && (listOfNewBikes.isNotEmpty() || (override && listOfBikesInFile.isNotEmpty()))
                    ) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Import") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Import"
                                )
                            },
                            onClick = {
                                importDataViewModel.commitImport {
//                                    Toast.makeText(context, "Date imported", Toast.LENGTH_LONG)
//                                        .show()
                                }
                                destinationsNavigator.navigateUp()
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                    } else {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Select file") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Select file"
                                )
                            },
                            onClick = {
                                launcher.launch()
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                    }
                }
            )
        }
    )
}