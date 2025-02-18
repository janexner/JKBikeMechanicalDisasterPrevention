package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.TemplateActivityAddViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultRideLevelSelector
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.RideLevel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun TemplateActivityAdd(
    templateActivityAddViewModel: TemplateActivityAddViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var rideLevel: Int? by remember { mutableStateOf(null) }
    var isEbikeSpecific by remember { mutableStateOf(false) }

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
                PageHeaderTextWithSpacer(stringResource(R.string.lbl_add_template_activity))
                DefaultRideLevelSelector(
                    rideLevel,
                    RideLevel.getListOfRideLevels()
                ) {
                    rideLevel = it
                    modified = true
                }
                DefaultSpacer()
                DefaultTextFieldWithSpacer(
                    value = title,
                    label = stringResource(R.string.lbl_activity_title),
                    placeholder = stringResource(R.string.title),
                    onValueChange = {
                        title = it
                        modified = true
                    },
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
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
                    checked = isEbikeSpecific
                ) {
                    isEbikeSpecific = it
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
                                val templateActivity = TemplateActivity(
                                    rideLevel = rideLevel,
                                    title = title,
                                    description = description,
                                    isEBikeSpecific = isEbikeSpecific
                                )
                                Log.d("TemplateActivityAdd", "Saving template activity $templateActivity")
                                templateActivityAddViewModel.saveTemplateActivity(templateActivity)
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