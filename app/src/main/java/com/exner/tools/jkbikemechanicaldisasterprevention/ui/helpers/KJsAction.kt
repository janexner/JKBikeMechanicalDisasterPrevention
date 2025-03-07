package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

import androidx.compose.ui.graphics.vector.ImageVector

data class KJsAction(
    val enabled: Boolean = true,
    val imageVector: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
)
