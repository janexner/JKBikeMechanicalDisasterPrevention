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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel

@Composable
fun ComponentWearLevelDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (WearLevel) -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(WearLevel.NEW, WearLevel.USED, WearLevel.DUE_FOR_REPLACEMENT)

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
                            label = { Text(text = label.toString()) }
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
                    Text(text = "Done")
                }
                TextButton(
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
