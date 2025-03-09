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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.RetirementReason
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.toLocalisedString
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations.pxToDp

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun RetirementReasonSelector(
    modifier: Modifier = Modifier,
    currentRetirementReason: RetirementReason?,
    displayLabel: Boolean = true,
    onRetirementReasonSelected: (RetirementReason?) -> Unit
) {
    var offset = Offset.Zero
    var retirementReasonsExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
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
                Text(text = stringResource(R.string.dropdown_lbl_which_retirement_reason))
                DefaultSpacer()
            }
            Button(
                onClick = { retirementReasonsExpanded = true }
            ) {
                if (currentRetirementReason != null) {
                    Text(text = toLocalisedString(currentRetirementReason, context))
                } else {
                    Text(text = stringResource(R.string.dropdown_trigger_select_a_wear_level))
                }
            }
        }
        val density = LocalDensity.current
        DropdownMenu(
            expanded = retirementReasonsExpanded,
            offset = DpOffset(pxToDp(offset.x, density), pxToDp(offset.y, density)),
            onDismissRequest = { retirementReasonsExpanded = false }) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.dropdown_item_unspecified_wear_level)) },
                onClick = {
                    onRetirementReasonSelected(null)
                    retirementReasonsExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.dropdown_item_retirement_reason_worn)) },
                onClick = {
                    onRetirementReasonSelected(RetirementReason.WORN)
                    retirementReasonsExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.dropdown_item_retirement_reason_crash)) },
                onClick = {
                    onRetirementReasonSelected(RetirementReason.CRASH)
                    retirementReasonsExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.dropdown_item_retirement_reason_upgrade)) },
                onClick = {
                    onRetirementReasonSelected(RetirementReason.UPGRADE)
                    retirementReasonsExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.dropdown_item_retirement_reason_no_longer_needed)) },
                onClick = {
                    onRetirementReasonSelected(RetirementReason.NO_LONGER_NEEDED)
                    retirementReasonsExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
        }
    }
}
