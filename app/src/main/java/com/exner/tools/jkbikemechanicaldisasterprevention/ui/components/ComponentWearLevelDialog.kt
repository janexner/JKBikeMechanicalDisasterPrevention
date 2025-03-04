package com.exner.tools.jkbikemechanicaldisasterprevention.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.toLocalisedString

@Composable
fun ComponentWearLevelDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (WearLevel) -> Unit,
    startIndex: Int = 0
) {
    var selectedIndex by remember { mutableIntStateOf(startIndex) }
    val options = listOf(WearLevel.NEW, WearLevel.USED, WearLevel.DUE_FOR_REPLACEMENT)

    val context = LocalContext.current

    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card(shape = RoundedCornerShape(8.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex,
                            label = { Text(text = toLocalisedString(
                                wearLevel = label,
                                context = context
                            )) }
                        )
                    }
                }
                DefaultSpacer()
                Button(
                    onClick = {
                        onConfirmation(options[selectedIndex])
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.dialog_btn_text_done))
                }
                TextButton(
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_btn_text_cancel),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
