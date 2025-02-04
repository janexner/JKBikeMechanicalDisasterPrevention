package com.exner.tools.kjdoitnow.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjdoitnow.R
import com.exner.tools.kjdoitnow.ui.TemplateActivityEditViewModel
import com.exner.tools.kjdoitnow.ui.components.DefaultSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.kjdoitnow.ui.components.IconSpacer
import com.exner.tools.kjdoitnow.ui.components.DefaultRideLevelSelector
import com.exner.tools.kjdoitnow.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.kjdoitnow.ui.components.TextAndSwitch
import com.exner.tools.kjdoitnow.ui.helpers.RideLevel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityDeleteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun TemplateActivityEdit(
    templateActivityUid: Long,
    destinationsNavigator: DestinationsNavigator,
) {

    val templateActivityEditViewModel =
        hiltViewModel<TemplateActivityEditViewModel, TemplateActivityEditViewModel.TemplateActivityEditViewModelFactory> { factory ->
            factory.create(templateActivityUid = templateActivityUid)
        }

    val templateActivity by templateActivityEditViewModel.templateActivity.observeAsState()

    var modified by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.imePadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                PageHeaderTextWithSpacer(stringResource(R.string.edit_template_activity))
                DefaultRideLevelSelector(
                    templateActivity?.rideLevel,
                    RideLevel.getListOfRideLevels(),
                ) {
                    templateActivityEditViewModel.updateRideLevel(it)
                    modified = true
                }
                DefaultSpacer()
                DefaultTextFieldWithSpacer(
                    value = templateActivity?.title ?: "",
                    label = stringResource(R.string.lbl_activity_title),
                    placeholder = stringResource(R.string.title),
                    onValueChange = {
                        templateActivityEditViewModel.updateTitle(it)
                        modified = true
                    },
                )
                OutlinedTextField(
                    value = templateActivity?.description ?: "",
                    onValueChange = {
                        templateActivityEditViewModel.updateDescription(it)
                        modified = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = stringResource(R.string.placehldr_description)) },
                    label = { Text(text = stringResource(R.string.lbl_description)) },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
                DefaultSpacer()
                TextAndSwitch(
                    text = stringResource(R.string.is_ebike_specific),
                    checked = templateActivity?.isEBikeSpecific ?: false
                ) {
                    templateActivityEditViewModel.updateIsEBikeSpecific(it)
                    modified = true
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
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }

                    IconSpacer()
                    IconButton(onClick = {
                        destinationsNavigator.navigate(
                            TemplateActivityDeleteDestination(
                                templateActivityUid
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
                floatingActionButton = {
                    if (modified) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = stringResource(R.string.save)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = stringResource(R.string.save_the_activity)
                                )
                            },
                            onClick = {
                                templateActivityEditViewModel.commitActivity()
                                modified = false
                                destinationsNavigator.navigateUp()
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