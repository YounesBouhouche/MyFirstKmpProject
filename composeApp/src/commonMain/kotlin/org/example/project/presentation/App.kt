package org.example.project.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.setSingletonImageLoaderFactory
import com.materialkolor.DynamicMaterialTheme
import org.example.project.presentation.routes.HomeScreen
import org.example.project.presentation.routes.SearchScreen
import org.example.project.presentation.util.AdaptiveNavigationScaffold
import org.example.project.util.BatteryManager
import org.example.project.util.getAsyncImageLoader
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun App(
    darkColorScheme: ColorScheme = MaterialTheme.colorScheme,
    lightColorScheme: ColorScheme = MaterialTheme.colorScheme,
    batteryManager: BatteryManager,
    pickFile: ((mimeType: String, onSave: (String) -> Unit) -> Unit)? = null,
    topBar: @Composable (content: @Composable () -> Unit) -> Unit = { it() },
    actions: @Composable RowScope.() -> Unit = {},
) {
    val viewModel = koinViewModel<MyViewModel>()
    val theme by viewModel.theme.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isDark = when(theme) {
        Themes.LIGHT -> false
        Themes.DARK -> true
        Themes.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }
    val colorScheme = if (isDark) darkColorScheme else lightColorScheme
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    DynamicMaterialTheme(
        primary = colorScheme.primary,
        secondary = colorScheme.secondary,
        tertiary = colorScheme.tertiary,
        error = colorScheme.error,
        animate = true,
        isDark = isDark,
//        motionScheme = MaterialTheme.motionScheme
    ) {
        Scaffold(
            topBar = {
                topBar {
                    TopAppBar(
                        title = {
                            Text(
                                "Compose Multiplatform App",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        actions = {
                            IconButton({
                                viewModel.switchTheme()
                            }) {
                                Icon(
                                    when(theme) {
                                        Themes.LIGHT -> Icons.Default.LightMode
                                        Themes.DARK -> Icons.Default.DarkMode
                                        Themes.SYSTEM_DEFAULT -> Icons.Default.BrightnessAuto
                                    },
                                    null
                                )
                            }
                            actions(this)
                        }
                    )
                }
            },
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars)
        ) { paddingValues ->
            AdaptiveNavigationScaffold(
                NavBarRoutes.entries,
                { it.icon },
                { it.label },
                { it.destination::class.qualifiedName ==
                        navBackStackEntry?.destination?.route },
                {
                    navController.navigate(it.destination) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                Modifier.fillMaxSize().padding(paddingValues)
            ) {
                NavHost(
                    navController,
                    Routes.Home,
                    Modifier.fillMaxSize()
                ) {
                    composable<Routes.Home> {
                        HomeScreen(viewModel, batteryManager)
                    }
                    composable<Routes.Search> {
                        SearchScreen(viewModel, pickFile)
                    }
                    composable<Routes.History> {

                    }
                }
            }
        }
    }
}