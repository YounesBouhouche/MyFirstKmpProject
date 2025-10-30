package org.example.project.presentation.util

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass

enum class NavigationType {
    ShortNavigationBarCompact,
    ShortNavigationBarMedium,
    NavigationRail,
    WideNavigationRail
}

fun navigationType(adaptiveInfo: WindowAdaptiveInfo): NavigationType {
    return with(adaptiveInfo) {
        if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            NavigationType.NavigationRail
        } else if (
            windowPosture.isTabletop ||
            windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)
        ) {
            NavigationType.ShortNavigationBarMedium
        } else {
            NavigationType.ShortNavigationBarCompact
        }
    }
}

fun showTwoPans(adaptiveInfo: WindowAdaptiveInfo): Boolean {
    return with(adaptiveInfo) {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) &&
        !windowPosture.isTabletop
    }
}