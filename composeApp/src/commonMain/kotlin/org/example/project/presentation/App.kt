package org.example.project.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.setSingletonImageLoaderFactory
import com.materialkolor.DynamicMaterialExpressiveTheme
import com.materialkolor.DynamicMaterialTheme
import myfirstkmpproject.composeapp.generated.resources.Res
import myfirstkmpproject.composeapp.generated.resources.battery_level
import myfirstkmpproject.composeapp.generated.resources.compose_multiplatform
import org.example.project.presentation.util.AdaptiveNavigationContainer
import org.example.project.presentation.util.AdaptiveNavigationScaffold
import org.example.project.presentation.util.ExpressiveButton
import org.example.project.presentation.util.MyImage
import org.example.project.util.BatteryManager
import org.example.project.util.ResourceView
import org.example.project.util.getAsyncImageLoader
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
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
    actions: @Composable RowScope.() -> Unit = {},
) {
    val viewModel = koinViewModel<MyViewModel>()
    val theme by viewModel.theme.collectAsState()
    val selectedPicture by viewModel.selectedPicture.collectAsState()
    val resource by viewModel.searchResult.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val clipboard = LocalClipboardManager.current
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
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
                AdaptiveNavigationContainer(
                    Modifier.fillMaxSize(),
                    showSecondPan = selectedPicture != null,
                    secondPan = {
                        selectedPicture?.let { picture ->
                            Column(
                                Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(bottom = 24.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Column(
                                    Modifier
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState())
                                        .padding(24.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    MyImage(
                                        model = picture.src.large,
                                        alt = picture.alt,
                                        shape = MaterialTheme.shapes.medium,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        clipboard.setText(AnnotatedString(picture.src.medium))
                                    }
                                    Text(picture.alt)
                                }
                                Row(
                                    Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    ExpressiveButton(
                                        ButtonDefaults.MediumContainerHeight,
                                        "Close",
                                        colors = ButtonDefaults.filledTonalButtonColors(),
                                        modifier = Modifier.fillMaxWidth().weight(1f),
                                        icon = Icons.Default.Close
                                    ) {
                                        viewModel.unselectPicture()
                                    }
                                    ExpressiveButton(
                                        ButtonDefaults.MediumContainerHeight,
                                        "Download",
                                        modifier = Modifier.fillMaxWidth().weight(1f),
                                        icon = Icons.Default.Download
                                    ) {
                                        pickFile?.invoke("image/jpg") { path ->
                                            viewModel.download(picture.src.original, path)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onHideSecondPan = {
                        viewModel.unselectPicture()
                    }
                ) {
                    NavHost(
                        navController,
                        Routes.Home,
                        Modifier.fillMaxSize()
                    ) {
                        composable<Routes.Home> {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painterResource(Res.drawable.compose_multiplatform),
                                    contentDescription = "Compose Multiplatform Logo",
                                    modifier = Modifier.size(200.dp)
                                )
                                Text(text = viewModel.getHelloWorld())
                                Text(text = stringResource(
                                    Res.string.battery_level,
                                    batteryManager.getBatteryLevel()
                                ))
                            }
                        }
                        composable<Routes.Search> {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(
                                    16.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                                ) {
                                    TextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        modifier = Modifier.weight(1f),
                                        placeholder = {
                                            Text("Search query")
                                        }
                                    )
                                    ExpressiveButton(
                                        text = "Search",
                                        size = ButtonDefaults.MediumContainerHeight
                                    )   {
                                        viewModel.searchPictures(searchQuery)
                                    }
                                }
                                ResourceView(
                                    resource,
                                    modifier = Modifier.fillMaxSize().weight(1f),
                                    idleContent = {
                                        Box(
                                            Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Enter a search query to find pictures")
                                        }
                                    },
                                    onCancel = viewModel::cancelSearch
                                ) { state ->
                                    Column(
                                        Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            "Found ${state.total_results} results"
                                        )
                                        LazyVerticalGrid(
                                            columns = GridCells.Adaptive(300.dp),
                                            modifier = Modifier.fillMaxSize().weight(1f),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            contentPadding = PaddingValues(16.dp)
                                        ) {
                                            state.photos.forEach { photo ->
                                                item {
                                                    MyImage(
                                                        model = photo.src.large,
                                                        alt = photo.alt,
                                                        shape = MaterialTheme.shapes.medium,
                                                        modifier = Modifier.size(300.dp)
                                                            .animateItem()
                                                    ) {
                                                        viewModel.selectPicture(photo)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        composable<Routes.History> {

                        }
                    }
                }
            }
        }
    }
}