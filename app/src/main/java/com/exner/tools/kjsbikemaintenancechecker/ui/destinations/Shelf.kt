package com.exner.tools.kjsbikemaintenancechecker.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.ui.ShelfViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.components.DefaultSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.components.IconSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ComponentAddDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun Shelf(
    shelfViewModel: ShelfViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator
) {

    val shelvedComponents: List<Component> by shelfViewModel.shelvedComponents.collectAsState(
        initial = emptyList()
    )

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
                Text(text = "This is your shelf. It contains components that are not currently attached to any bike.")
                DefaultSpacer()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(shelvedComponents, key = { "component.${it.uid}" }) { component ->
                        Surface(
                            onClick = {
                                destinationsNavigator.navigate(com.ramcosta.composedestinations.generated.destinations.ComponentEditDestination(component.uid))
                            },
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Dataset, // TODO
                                    contentDescription = "Component",
                                )
                                IconSpacer()
                                Column {
                                    Text(
                                        text = component.name,
                                    )
                                    Text(
                                        text = component.description,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
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
                            contentDescription = "Back"
                        )
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Add component") },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add component"
                            )
                        },
                        onClick = {
                            destinationsNavigator.navigate(ComponentAddDestination(bikeUid = null))
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            )
        }
    )
}