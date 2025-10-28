package org.example.project

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.setSingletonImageLoaderFactory
import myfirstkmpproject.composeapp.generated.resources.Res
import myfirstkmpproject.composeapp.generated.resources.battery_level
import myfirstkmpproject.composeapp.generated.resources.compose_multiplatform
import org.example.project.dependencies.MyViewModel
import org.example.project.util.ResourceView
import org.example.project.util.getAsyncImageLoader
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun App(batteryManager: BatteryManager) {
    val viewModel = koinViewModel<MyViewModel>()
    val resource by viewModel.searchResult.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val clipboard = LocalClipboardManager.current
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    MaterialExpressiveTheme {
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text("Compose Multiplatform App")
                    },
                    navigationIcon = {
                        AnimatedContent(navBackStackEntry?.destination?.route ?: "home") { route ->
                            if (route != "home") {
                                IconButton({ navController.navigateUp() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back to Home"
                                    )
                                }
                            }
                        }
                    }
                )
            }
        ) {
            NavHost(navController, "home", Modifier.fillMaxSize().padding(it)) {
                composable("home") {
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
                        Button(
                            { navController.navigate("search") },
                            ButtonDefaults.shapesFor(ButtonDefaults.MediumContainerHeight),
                            contentPadding = ButtonDefaults.contentPaddingFor(ButtonDefaults.MediumContainerHeight),
                        ) {
                            Text("Go to Search")
                        }
                    }
                }
                composable("search") {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ResourceView(
                            resource,
                            idleContent = {
                                Column(
                                    Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                                ) {
                                    TextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                    )
                                    Button({
                                        viewModel.searchPictures(searchQuery)
                                    }) {
                                        Text("Search")
                                    }
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
                                    columns = GridCells.Adaptive(150.dp),
                                    modifier = Modifier.fillMaxSize().weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    state.photos.forEach { photo ->
                                        item {
                                            SubcomposeAsyncImage(
                                                model = photo.src.large,
                                                contentDescription = photo.alt,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .clip(MaterialTheme.shapes.medium)
                                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                                    .clickable {
                                                        clipboard.setText(AnnotatedString(photo.src.medium))
                                                    }
                                                    .size(100.dp),
                                                loading = {
                                                    Box(
                                                        Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator(
                                                            Modifier.size(24.dp)
                                                        )
                                                    }
                                                },
                                                error = {
                                                    Text(photo.alt)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}