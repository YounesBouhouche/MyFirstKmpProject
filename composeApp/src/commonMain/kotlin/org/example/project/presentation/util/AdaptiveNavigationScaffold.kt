package org.example.project.presentation.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailItemDefaults
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun<T> AdaptiveNavigationScaffold(
    navigationItems: List<T>,
    navItemIcon: (T) -> ImageVector,
    navItemLabel: (T) -> String,
    selected: (T) -> Boolean,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = rememberWideNavigationRailState()
    val navigationType = navigationType(currentWindowAdaptiveInfo(true))
    val showNavRail = navigationType in setOf(
        NavigationType.NavigationRail,
        NavigationType.WideNavigationRail
    )
    Scaffold(modifier) {
        Row(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = showNavRail,
                enter = expandHorizontally(expandFrom = Alignment.Start),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start),
                modifier = Modifier.fillMaxHeight(),
            ) {
                WideNavigationRail(
                    state = state,
                    arrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    header = {
                        IconButton({
                            scope.launch {
                                if (state.currentValue == WideNavigationRailValue.Expanded) {
                                    state.collapse()
                                } else {
                                    state.expand()
                                }
                            }
                        },
                            Modifier.offset(x = 24.dp)
                        ) {
                            Icon(
                                if (state.currentValue == WideNavigationRailValue.Expanded) {
                                    Icons.Default.Menu
                                } else {
                                    Icons.Default.Menu
                                },
                                null
                            )
                        }
                    }
                ) {
                    val expanded = state.currentValue == WideNavigationRailValue.Expanded
                    navigationItems.forEach {
                        WideNavigationRailItem(
                            icon = { Icon(navItemIcon(it), null) },
                            label = {
                                Text(navItemLabel(it))
                            },
                            selected = selected(it),
                            onClick = { onItemSelected(it) },
                            railExpanded = expanded
                        )
                    }
                }
            }
            Column(Modifier.fillMaxSize().weight(1f)) {
                Box(Modifier.fillMaxSize().weight(1f)) {
                    content()
                }
                AnimatedVisibility(
                    visible = !showNavRail,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NavigationBar {
                        navigationItems.forEach { item ->
                            NavigationBarItem(
                                icon = { Icon(navItemIcon(item), null) },
                                label = { Text(navItemLabel(item)) },
                                selected = selected(item),
                                onClick = { onItemSelected(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AdaptiveNavigationScaffoldPreview() {
    AdaptiveNavigationScaffold(
        navigationItems = listOf("Home", "Search", "History"),
        navItemIcon = { Icons.Default.Home },
        navItemLabel = { it },
        selected = { it == "Home" },
        onItemSelected = {}
    ) {
        Box(Modifier.fillMaxSize()) {
            Text("Content goes here")
        }
    }
}