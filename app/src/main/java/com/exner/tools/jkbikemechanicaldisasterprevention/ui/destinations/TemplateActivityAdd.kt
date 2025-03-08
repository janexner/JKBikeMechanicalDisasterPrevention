package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultRideLevelSelectorTemplate
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultTextFieldWithSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.TextAndSwitch
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.TemplateActivityAddDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun TemplateActivityAdd(
    templateActivityAddViewModel: TemplateActivityAddViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var rideLevel: Int? by remember { mutableStateOf(null) }
    var isEbikeSpecific by remember { mutableStateOf(false) }

    var modified by remember { mutableStateOf(false) }

    KJsResponsiveNavigation(
        TemplateActivityAddDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.btn_text_cancel),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            )
        ),
        myFloatingActionButton = KJsAction(
            imageVector = Icons.Default.Save,
            contentDescription = stringResource(R.string.btn_text_save),
            onClick = {
                val templateActivity = TemplateActivity(
                    rideLevel = rideLevel,
                    title = title,
                    description = description,
                    isEBikeSpecific = isEbikeSpecific
                )
                Log.d(
                    "TemplateActivityAdd",
                    "Saving template activity $templateActivity"
                )
                templateActivityAddViewModel.saveTemplateActivity(
                    templateActivity
                )
                modified = false
                destinationsNavigator.navigateUp()
            },
            enabled = modified && title.isNotBlank()
        ),
        headline = stringResource(R.string.hdr_add_template_activity)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                DefaultRideLevelSelectorTemplate(
                    rideLevel,
                ) {
                    rideLevel = it
                    modified = true
                }
                DefaultSpacer()
                DefaultTextFieldWithSpacer(
                    value = title,
                    label = stringResource(R.string.lbl_activity_title),
                    placeholder = stringResource(R.string.placehldr_title),
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
        }
    }
}