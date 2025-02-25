package com.exner.tools.jkbikemechanicaldisasterprevention.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations.pxToDp

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun BikeSelector(
    currentBike: Bike?,
    bikes: List<Bike>,
    displayLabel: Boolean = true,
    modifier: Modifier = Modifier,
    onBikeSelected: (Bike?) -> Unit
) {
    var offset = Offset.Zero
    var bikesExpanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
            .padding(8.dp, 0.dp)
            .wrapContentSize(Alignment.TopEnd)
            .pointerInteropFilter {
                offset = Offset(it.x, it.y)
                false
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (displayLabel) {
                Text(text = stringResource(R.string.which_bike))
                DefaultSpacer()
            }
            Button(
                onClick = { bikesExpanded = true }
            ) {
                if (currentBike != null) {
                    Text(text = currentBike.name)
                } else {
                    Text(text = stringResource(R.string.select_a_bike))
                }
            }
        }
        val density = LocalDensity.current
        DropdownMenu(
            expanded = bikesExpanded,
            offset = DpOffset(pxToDp(offset.x, density), pxToDp(offset.y, density)),
            onDismissRequest = { bikesExpanded = false }) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.all_bikes)) },
                onClick = {
                    onBikeSelected(null)
                    bikesExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            bikes.forEach { bike ->
                DropdownMenuItem(
                    text = { Text(text = bike.name) },
                    onClick = {
                        onBikeSelected(bike)
                        bikesExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

