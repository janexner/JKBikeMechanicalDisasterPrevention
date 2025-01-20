package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme

@Composable
fun TextAndTriStateToggle(
    text: String,
    currentTheme: Theme,
    updateTheme: (Theme) -> Unit
) {
    val states = listOf(
        Theme.Auto,
        Theme.Dark,
        Theme.Light,
    )

    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            Surface(
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    states.forEach { thisTheme ->
                        Text(
                            text = thisTheme.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(50))
                                .clickable {
                                    updateTheme(thisTheme)
                                }
                                .background(
                                    if (thisTheme == currentTheme) {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    }
                                )
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp,
                                ),
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DefaultSpacer() {
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun IconSpacer() {
    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
}
