package com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class NavigationStyle {
    BOTTOM_BAR, LEFT_RAIL, LEFT_DRAWER;

    companion object {
        fun getNavigationStyleForWidthSizeClass(widthSizeClass: WindowWidthSizeClass): NavigationStyle {
            return when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    NavigationStyle.BOTTOM_BAR
                }

                WindowWidthSizeClass.Medium -> {
                    NavigationStyle.LEFT_RAIL
                }

                WindowWidthSizeClass.Expanded -> {
                    NavigationStyle.LEFT_DRAWER
                }

                else -> {
                    NavigationStyle.BOTTOM_BAR
                }
            }
        }
    }
}

