package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class NavigationStyle {
    BOTTOM_BAR, LEFT_RAIL, LEFT_DRAWER;

    companion object {
        fun getNavigationStyleForWidthSizeClass(widthSizeClass: WindowWidthSizeClass): NavigationStyle {
            return when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    BOTTOM_BAR
                }

                WindowWidthSizeClass.Medium -> {
                    LEFT_RAIL
                }

                WindowWidthSizeClass.Expanded -> {
                    LEFT_DRAWER
                }

                else -> {
                    BOTTOM_BAR
                }
            }
        }
    }
}

