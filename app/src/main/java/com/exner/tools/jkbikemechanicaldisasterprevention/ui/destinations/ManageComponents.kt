package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.ManageComponentsViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.IconSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ComponentAddDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentAnalysisDestination
import com.ramcosta.composedestinations.generated.destinations.ComponentEditDestination
import com.ramcosta.composedestinations.generated.destinations.ManageComponentsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination<RootGraph>
@Composable
fun ManageComponents(
    manageComponentsViewModel: ManageComponentsViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {

    KJsResponsiveNavigation(
        ManageComponentsDestination,
        destinationsNavigator,
        windowSizeClass
    ) {
        val components: List<Component> by manageComponentsViewModel.components.collectAsState(
            initial = emptyList()
        )

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            PageHeaderTextWithSpacer(stringResource(R.string.hdr_manage_components))
            Text(text = stringResource(R.string.tap_component_to_edit))
            DefaultSpacer()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        text = stringResource(R.string.hdr_components),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                items(components, key = { "component.${it.uid}" }) { component ->
                    Surface(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                    destinationsNavigator.navigate(
                                        ComponentEditDestination(
                                            component.uid
                                        )
                                    )
                                },
                            )
                            .padding(4.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dataset,
                                contentDescription = stringResource(R.string.component),
                            )
                            IconSpacer()
                            Text(
                                text = component.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            DefaultSpacer()
                            Text(
                                text = component.description,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
            DefaultSpacer()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    destinationsNavigator.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Button(onClick = {
                    destinationsNavigator.navigate(ComponentAnalysisDestination)
                }) {
                    Text(
                        text = stringResource(R.string.hdr_analyse_components)
                    )
                }
                if (windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact) {
                    Button(onClick = {
                        destinationsNavigator.navigate(ComponentAddDestination)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.btn_desc_add_component)
                        )
                        IconSpacer()
                        Text(text = stringResource(R.string.btn_text_add_component))
                    }
                }
            }
        }
    }
}