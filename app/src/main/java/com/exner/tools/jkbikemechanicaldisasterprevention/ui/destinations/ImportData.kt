package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportDataViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportState
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ImportStateConstants
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
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
        headline = stringResource(R.string.hdr_import_data)
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
        val overrideClashingBikes by importDataViewModel.overrideClashingBikes.collectAsStateWithLifecycle()

        val listOfActivitiesInFile by importDataViewModel.listOfActivitiesInFile.collectAsStateWithLifecycle()
        val listOfActivitiesOld by importDataViewModel.listOfActivitiesOld.collectAsStateWithLifecycle()
        val listOfActivitiesNew by importDataViewModel.listOfActivitiesNew.collectAsStateWithLifecycle()
        val listOfActivitiesClashing by importDataViewModel.listOfActivitiesClashing.collectAsStateWithLifecycle()
        val overrideClashingActivities by importDataViewModel.overrideClashingActivities.collectAsStateWithLifecycle()

        val listOfTemplateActivitiesInFile by importDataViewModel.listOfTemplateActivitiesInFile.collectAsStateWithLifecycle()
        val listOfTemplateActivitiesOld by importDataViewModel.listOfTemplateActivitiesOld.collectAsStateWithLifecycle()
        val listOfTemplateActivitiesNew by importDataViewModel.listOfTemplateActivitiesNew.collectAsStateWithLifecycle()
        val listOfTemplateActivitiesClashing by importDataViewModel.listOfTemplateActivitiesClashing.collectAsStateWithLifecycle()
        val overrideClashingTemplateActivities by importDataViewModel.overrideClashingTemplateActivities.collectAsStateWithLifecycle()

        val listOfComponentsInFile by importDataViewModel.listOfComponentsInFile.collectAsStateWithLifecycle()
        val listOfComponentsOld by importDataViewModel.listOfComponentsOld.collectAsStateWithLifecycle()
        val listOfComponentsNew by importDataViewModel.listOfComponentsNew.collectAsStateWithLifecycle()
        val listOfComponentsClashing by importDataViewModel.listOfComponentsClashing.collectAsStateWithLifecycle()
        val overrideClashingComponents by importDataViewModel.overrideClashingComponents.collectAsStateWithLifecycle()

        val launcher = rememberFilePickerLauncher(
            type = PickerType.File(
                extensions = listOf("json")
            ),
            mode = PickerMode.Single,
            title = stringResource(R.string.pick_a_json_file)
        ) { file ->
            importDataViewModel.setFile(file)
        }

        val showOverrides by importDataViewModel.showOverrideControls.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // step 1 - select a file
            stickyHeader {
                Column {
                    when (importState.state) {
                        ImportStateConstants.IDLE -> {
                            Text(text = stringResource(R.string.start_by_selecting_a_file_to_import_from))
                            DefaultSpacer()
                            Button(onClick = {
                                launcher.launch()
                            }) {
                                Text(text = stringResource(R.string.btn_text_select_file))
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
                                Text(text = stringResource(R.string.btn_text_select_different_file))
                            }
                        }

                        ImportStateConstants.FILE_SELECTED, ImportStateConstants.FILE_ANALYSED -> {
                            Text(text = stringResource(R.string.file_selected) + fileForImport?.name)
                            DefaultSpacer()
                            Button(onClick = {
                                launcher.launch()
                            }) {
                                Text(text = stringResource(R.string.btn_text_select_different_file))
                            }
                        }

                        ImportStateConstants.IMPORT_FINISHED -> {
                            Text(text = stringResource(R.string.btn_text_import_data))
                            DefaultSpacer()
                            Button(onClick = {
                                launcher.launch()
                            }) {
                                Text(text = stringResource(R.string.btn_text_select_different_file))
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
                        text = stringResource(R.string.the_file_contains)
                                + " "
                                + listOfBikesInFile.size
                                + " "
                                + stringResource(R.string.bikes)
                                + ", "
                                + listOfActivitiesInFile.size
                                + " "
                                + stringResource(R.string.activities)
                                + ", "
                                + listOfTemplateActivitiesInFile.size
                                + " "
                                + stringResource(R.string.template_activities)
                                + ", "
                                + listOfComponentsInFile.size
                                + " "
                                + stringResource(R.string.components)
                    )
                }
            }

            // step 3 - stuff
            item {
                if (listOfBikesOld.isNotEmpty() || listOfBikesClashing.isNotEmpty()) {
                    var text =
                        stringResource(R.string.hdr_bikes) + ": ${listOfBikesOld.size} " + stringResource(
                            R.string.already_in_database
                        )
                    if (listOfBikesClashing.isNotEmpty()) {
                        text += ", ${listOfBikesClashing.size} " + stringResource(R.string.clashing)
                    }
                    text += if (listOfBikesNew.isEmpty()) {
                        ". " + stringResource(R.string.nothing_to_import)
                    } else {
                        "."
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                        text = text
                    )
                }
                if (listOfActivitiesOld.isNotEmpty() || listOfActivitiesClashing.isNotEmpty()) {
                    var text =
                        stringResource(R.string.hdr_activities) + ": ${listOfActivitiesOld.size} " + stringResource(
                            R.string.already_in_database
                        )
                    if (listOfActivitiesClashing.isNotEmpty()) {
                        text += ", ${listOfActivitiesClashing.size} " + stringResource(R.string.clashing)
                    }
                    text += if (listOfActivitiesNew.isEmpty()) {
                        ". " + stringResource(R.string.nothing_to_import)
                    } else {
                        "."
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                        text = text
                    )
                }
                if (listOfTemplateActivitiesOld.isNotEmpty() || listOfTemplateActivitiesClashing.isNotEmpty()) {
                    var text =
                        stringResource(R.string.hdr_template_activities) + ": ${listOfTemplateActivitiesOld.size} " + stringResource(
                            R.string.already_in_database
                        )
                    if (listOfTemplateActivitiesClashing.isNotEmpty()) {
                        text += ", ${listOfTemplateActivitiesClashing.size} " + stringResource(R.string.clashing)
                    }
                    text += if (listOfTemplateActivitiesNew.isEmpty()) {
                        ". " + stringResource(R.string.nothing_to_import)
                    } else {
                        "."
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                        text = text
                    )
                }
                if (listOfComponentsOld.isNotEmpty() || listOfComponentsClashing.isNotEmpty()) {
                    var text =
                        stringResource(R.string.hdr_components) + ": ${listOfComponentsOld.size} " + stringResource(
                            R.string.already_in_database
                        )
                    if (listOfComponentsClashing.isNotEmpty()) {
                        text += ", ${listOfComponentsClashing.size} " + stringResource(R.string.clashing)
                    }
                    text += if (listOfComponentsNew.isEmpty()) {
                        ". " + stringResource(R.string.nothing_to_import)
                    } else {
                        "."
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                        text = text
                    )
                }
            }

            // step 3a - bikes
            stickyHeader {
                if (listOfBikesNew.isNotEmpty()) {
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
                if (listOfBikesNew.isNotEmpty()) {
                    listOfBikesNew.forEach {
                        Text(text = it.name)
                    }
                }
            }
            item {
                if (listOfBikesNew.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            importDataViewModel.importNewBikes()
                        }) {
                            Text(stringResource(R.string.btn_text_import_these_bikes))
                        }
                    }
                }
            }

            // step 3b - activities
            stickyHeader {
                if (listOfActivitiesNew.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_activities),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                if (listOfActivitiesNew.isNotEmpty()) {
                    listOfActivitiesNew.forEach {
                        Text(text = it.title)
                    }
                }
            }
            item {
                if (listOfActivitiesNew.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            importDataViewModel.importNewActivities()
                        }) {
                            Text(stringResource(R.string.btn_text_import_these_activities))
                        }
                    }
                }
            }

            // step 3c - templates
            stickyHeader {
                if (listOfTemplateActivitiesNew.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_template_activities),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                if (listOfTemplateActivitiesNew.isNotEmpty()) {
                    listOfTemplateActivitiesNew.forEach {
                        Text(text = it.title)
                    }
                }
            }
            item {
                if (listOfTemplateActivitiesNew.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            importDataViewModel.importNewTemplateActivities()
                        }) {
                            Text(stringResource(R.string.btn_text_import_these_template_activities))
                        }
                    }
                }
            }

            // step 3d - components
            stickyHeader {
                if (listOfComponentsNew.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_components),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                if (listOfComponentsNew.isNotEmpty()) {
                    listOfComponentsNew.forEach {
                        Text(text = it.name)
                    }
                }
            }
            item {
                if (listOfComponentsNew.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            importDataViewModel.importNewComponents()
                        }) {
                            Text(stringResource(R.string.btn_text_import_these_components))
                        }
                    }
                }
            }

            // done
            // or are we?
            item {
                if (listOfBikesClashing.isNotEmpty() || listOfActivitiesClashing.isNotEmpty() || listOfTemplateActivitiesClashing.isNotEmpty()) {
                    TextAndSwitch(
                        text = stringResource(R.string.overwrite_any),
                        checked = showOverrides
                    ) {
                        importDataViewModel.setShowOverrideControls(it)
                    }
                }
            }

            // overwrite bikes?
            item {
                if (showOverrides && listOfBikesClashing.isNotEmpty()) {
                    TextAndSwitch(
                        text = stringResource(R.string.overwrite_bikes),
                        checked = overrideClashingBikes
                    ) {
                        importDataViewModel.setOverrideClashingBikes(it)
                    }
                }
            }

            // overwrite activities?
            item {
                if (showOverrides && listOfActivitiesClashing.isNotEmpty()) {
                    TextAndSwitch(
                        text = stringResource(R.string.overwrite_activities),
                        checked = overrideClashingActivities
                    ) {
                        importDataViewModel.setOverrideClashingActivities(it)
                    }
                }
            }

            // overwrite template activities?
            item {
                if (showOverrides && listOfTemplateActivitiesClashing.isNotEmpty()) {
                    TextAndSwitch(
                        text = stringResource(R.string.overwrite_template_activities),
                        checked = overrideClashingTemplateActivities
                    ) {
                        importDataViewModel.setOverrideClashingTemplateActivities(it)
                    }
                }
            }

            // overwrite components?
            item {
                if (showOverrides && listOfComponentsClashing.isNotEmpty()) {
                    TextAndSwitch(
                        text = stringResource(R.string.overwrite_components),
                        checked = overrideClashingComponents
                    ) {
                        importDataViewModel.setOverrideClashingComponents(it)
                    }
                }
            }

            // button
            item {
                if (overrideClashingActivities || overrideClashingTemplateActivities || overrideClashingBikes || overrideClashingComponents) {
                    Button(onClick = {
                        // overwrite!
                        importDataViewModel.overwriteData()
                    }) {
                        Text(stringResource(R.string.btn_text_overwrite_data))
                    }
                }
            }
        }
    }
}