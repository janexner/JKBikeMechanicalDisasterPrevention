package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.TemplateActivityEditViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultRideLevelSelectorTemplate
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityDeleteDestination
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityEditDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun TemplateActivityEdit(
    templateActivityUid: Long,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    val templateActivityEditViewModel =
        hiltViewModel<TemplateActivityEditViewModel, TemplateActivityEditViewModel.TemplateActivityEditViewModelFactory> { factory ->
            factory.create(templateActivityUid = templateActivityUid)
        }

    var modified by remember { mutableStateOf(false) }

    KJsResponsiveNavigation(
        TemplateActivityEditDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_text_cancel),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            ),
            KJsAction(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.btn_text_delete),
                onClick = {
                    destinationsNavigator.navigate(
                        TemplateActivityDeleteDestination(
                            templateActivityUid
                        )
                    )
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(R.string.btn_text_save),
            onClick = {
                templateActivityEditViewModel.commitActivity()
                modified = false
                destinationsNavigator.navigateUp()
            },
            enabled = modified
        )
    ) {
        val templateActivity by templateActivityEditViewModel.templateActivity.observeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            PageHeaderTextWithSpacer(stringResource(R.string.hdr_edit_template_activity))
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                DefaultRideLevelSelectorTemplate(
                    templateActivity?.rideLevel,
                ) {
                    templateActivityEditViewModel.updateRideLevel(it)
                    modified = true
                }
                DefaultSpacer()
                DefaultTextFieldWithSpacer(
                    value = templateActivity?.title ?: "",
                    label = stringResource(R.string.lbl_activity_title),
                    placeholder = stringResource(R.string.placehldr_title),
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
        }
    }
}
