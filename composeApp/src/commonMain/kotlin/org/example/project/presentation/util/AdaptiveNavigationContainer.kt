package org.example.project.presentation.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AdaptiveNavigationContainer(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    secondPan: (@Composable () -> Unit)? = null,
    showSecondPan: Boolean = false,
    onHideSecondPan: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val showTwoPans = showTwoPans(currentWindowAdaptiveInfo(true))
    val switchToSecondPan = !showTwoPans && showSecondPan
    Row(modifier.onKeyEvent {
        if (it.key == Key.Escape) {
            onHideSecondPan()
            return@onKeyEvent false
        }
        true
    }) {
        Scaffold(
            Modifier.fillMaxSize().weight(1f),
            topBar = topBar
        ) { paddingValues ->
            val navController = rememberNavController()
            val destination = navController.currentBackStackEntryAsState().value?.destination
            LaunchedEffect(switchToSecondPan) {
                if (switchToSecondPan)
                    navController.navigate("secondPan")
                else if (navController.currentDestination?.route == "secondPan")
                    try {
                        navController.navigateUp()
                    } catch (_: Exception) {

                    }
            }
            LaunchedEffect(destination) {
                if (destination?.route == "mainContent")
                    onHideSecondPan()
            }
            NavHost(
                navController,
                startDestination = "mainContent",
                enterTransition = {
                    if (targetState.destination.route == "secondPan") {
                        slideInHorizontally { it }
                    } else {
                        slideInHorizontally { -it }
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "secondPan") {
                        slideOutHorizontally { -it }
                    } else {
                        slideOutHorizontally { it }
                    }
                }
            ) {
                composable("mainContent") {
                    Box(Modifier.fillMaxSize()) {
                        content(paddingValues)
                    }
                }
                composable("secondPan") {
                    Box(Modifier.fillMaxSize()) {
                        secondPan?.invoke()
                    }
                }
            }
        }
        secondPan?.let {
            AnimatedVisibility(
                showTwoPans and showSecondPan,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it },
                modifier = Modifier.fillMaxSize().weight(1f)
            ) {
                secondPan()
            }
        }
    }
}