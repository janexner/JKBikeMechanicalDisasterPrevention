package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportDataViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportState
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportStateConstants
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ImportDataDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ImportData(
    importDataViewModel: ImportDataViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        ImportDataDestination,
        destinationsNavigator,
        windowSizeClass
    ) {
        val importState by importDataViewModel.importStateFlow.collectAsStateWithLifecycle(
            ImportState()
        )
        val errorMessage by importDataViewModel.errorMessage.collectAsStateWithLifecycle()

        val fileForImport by importDataViewModel.file.collectAsStateWithLifecycle()

        val listOfBikesInFile by importDataViewModel.listOfBikesInFile.collectAsStateWithLifecycle()
        val listOfBikesOld by importDataViewModel.listOfBikesOld.collectAsStateWithLifecycle()
        val listOfBikesNew by importDataViewModel.listOfBikesNew.collectAsStateWithLifecycle()
        val listOfBikesClashing by importDataViewModel.listOfBikesClashing.collectAsStateWithLifecycle()
        var overrideClashingBikes by remember { mutableStateOf(false) }

        val listOfActivitiesInFile by importDataViewModel.listOfActivitiesInFile.collectAsStateWithLifecycle()
        val listOfActivitiesOld by importDataViewModel.listOfActivitiesOld.collectAsStateWithLifecycle()
        val listOfActivitiesNew by importDataViewModel.listOfActivitiesNew.collectAsStateWithLifecycle()
        val listOfActivitiesClashing by importDataViewModel.listOfActivitiesClashing.collectAsStateWithLifecycle()
        var overrideClashingActivities by remember { mutableStateOf(false) }

        val listOfTemplateActivitiesInFile by importDataViewModel.listOfActivitiesInFile.collectAsStateWithLifecycle()
        val listOfTemplateActivitiesOld by importDataViewModel.listOfActivitiesOld.collectAsStateWithLifecycle()
        val listOfTemplateActivitiesNew by importDataViewModel.listOfActivitiesNew.collectAsStateWithLifecycle()
        val listOfTemplateActivitiesClashing by importDataViewModel.listOfActivitiesClashing.collectAsStateWithLifecycle()
        var overrideClashingTemplateActivities by remember { mutableStateOf(false) }

        val launcher = rememberFilePickerLauncher(
            type = PickerType.File(
                extensions = listOf("json")
            ),
            mode = PickerMode.Single,
            title = stringResource(R.string.pick_a_json_file)
        ) { file ->
            importDataViewModel.setFile(file)
        }

        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            // step 1 - select a file
            stickyHeader {
                Column() {
                    when (importState.state) {
                        ImportStateConstants.IDLE -> {
                            Text(text = stringResource(R.string.start_by_selecting_a_file_to_import_from))
                            DefaultSpacer()
                            Button(onClick = {
                                launcher.launch()
                            }) {
                                Text(text = stringResource(R.string.select_file))
                            }
                        }

                        ImportStateConstants.ERROR -> {
                            Text(text = stringResource(R.string.something_went_wrong))
                            DefaultSpacer()
                            Text(text = errorMessage)
                            DefaultSpacer()
                            Text(text = stringResource(R.string.you_could_try_importing_a_different_file))
                            DefaultSpacer()
                            Button(onClick = {
                                launcher.launch()
                            }) {
                                Text(text = stringResource(R.string.select_different_file))
                            }
                        }

                        ImportStateConstants.FILE_SELECTED, ImportStateConstants.FILE_ANALYSED -> {
                            Text(text = stringResource(R.string.file_selected) + fileForImport?.name)
                            DefaultSpacer()
                            Button(onClick = {
                                launcher.launch()
                            }) {
                                Text(text = stringResource(R.string.select_different_file))
                            }
                        }

                        ImportStateConstants.IMPORT_FINISHED -> {
                            Text(text = stringResource(R.string.import_data))
                            DefaultSpacer()
                            Button(onClick = {
                                launcher.launch()
                            }) {
                                Text(text = stringResource(R.string.select_different_file))
                            }
                        }
                    }
                }
            }

            // step 2 - information about the file
            item {
                if (importState.state != ImportStateConstants.IDLE) {
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                        text = "The file contains " + listOfBikesInFile.size + " bikes, " + listOfActivitiesInFile.size + " activities, and " + listOfTemplateActivitiesInFile.size + " template activities."
                    )
                }
            }

            // step 3 - stuff

            // step 3a - bikes
            stickyHeader {
                if (listOfBikesInFile.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_bikes),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                if (listOfBikesInFile.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(text = "New")
                            listOfBikesNew.forEach {
                                Text(text = it.name)
                            }
                        }
                        DefaultSpacer()
                        Column {
                            Text(text = "Existing")
                            listOfBikesOld.forEach {
                                Text(text = it.name)
                            }
                        }
                        DefaultSpacer()
                        Column {
                            Text(text = "Clashing")
                            listOfBikesClashing.forEach {
                                Text(text = it.name)
                            }
                        }
                    }
                }
            }
            item {
                if (listOfBikesNew.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextAndSwitch(
                            text = "Overwrite clashing bikes?",
                            checked = overrideClashingBikes
                        ) {
                            overrideClashingBikes = it
                        }
                        DefaultSpacer()
                        Button(onClick = {

                        }) {
                            Text("Import these bikes")
                        }
                    }
                }
            }

            // step 3b - activities

            // step 3c - templates

            // done
        }
    }
}