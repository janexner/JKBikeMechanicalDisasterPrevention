package com.exner.tools.jkbikemechanicaldisasterprevention.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.RideLevel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultRideLevelSelector(
    currentRideLevel: Int?,
    rideLevels: List<Int>,
    onItemSelected: (Int?) -> Unit,
) {
    val context = LocalContext.current

    var levelsExpanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.template_for_ride_level))
            DefaultSpacer()
            Box {
                Button(
                    onClick = { levelsExpanded = true }
                ) {
                    if (currentRideLevel != null) {
                        Text(text = RideLevel.getLabel(context, currentRideLevel))
                    } else {
                        Text(text = stringResource(R.string.select_a_level))
                    }
                }
                DropdownMenu(
                    expanded = levelsExpanded,
                    onDismissRequest = { levelsExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.all_levels)) },
                        onClick = {
                            onItemSelected(null)
                            levelsExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                    rideLevels.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(text = RideLevel.getLabel(context, level)) },
                            onClick = {
                                onItemSelected(level)
                                levelsExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    }
    DefaultSpacer()
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RideLevelSelectorForLists(
    currentRideLevel: Int?,
    rideLevels: List<Int>,
    onItemSelected: (Int?) -> Unit,
) {
    val context = LocalContext.current

    var levelsExpanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp)
            .wrapContentSize(Alignment.TopEnd)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.ride_level_name))
            DefaultSpacer()
            Box {
                Button(
                    onClick = { levelsExpanded = true }
                ) {
                    if (currentRideLevel != null) {
                        Text(text = RideLevel.getLabel(context, currentRideLevel))
                    } else {
                        Text(text = stringResource(R.string.all_levels))
                    }
                }
                DropdownMenu(
                    expanded = levelsExpanded,
                    onDismissRequest = { levelsExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.all_levels)) },
                        onClick = {
                            onItemSelected(null)
                            levelsExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                    rideLevels.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(text = RideLevel.getLabel(context, level)) },
                            onClick = {
                                onItemSelected(level)
                                levelsExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    }
    DefaultSpacer()
}