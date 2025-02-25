package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

import androidx.compose.ui.graphics.vector.ImageVector

data class KJsMenuItem(
    val label: String,
    val icon: ImageVector,
    val selected: Boolean = false,
    val onClick: () -> Unit
)
