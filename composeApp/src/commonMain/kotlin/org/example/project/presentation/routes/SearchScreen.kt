package org.example.project.presentation.routes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.example.project.presentation.MyViewModel
import org.example.project.presentation.Status
import org.example.project.presentation.util.AdaptiveNavigationContainer
import org.example.project.presentation.util.ExpressiveButton
import org.example.project.presentation.util.MyImage
import org.example.project.util.ResourceView

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchScreen(
    viewModel: MyViewModel,
    pickFile: ((mimeType: String, onSave: (String) -> Unit) -> Unit)? = null,
) {
    val resource by viewModel.searchResult.collectAsState()
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    val selectedPicture by viewModel.selectedPicture.collectAsState()
    var picture by remember { mutableStateOf(selectedPicture) }
    LaunchedEffect(selectedPicture) {
        if (selectedPicture != null)
            picture = selectedPicture
    }
    val clipboard = LocalClipboardManager.current
    val downloadState by viewModel.downloadState.collectAsState()
    AdaptiveNavigationContainer(
        Modifier.fillMaxSize(),
        showSecondPan = selectedPicture != null,
        secondPan = {
            picture?.let { picture ->
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
                    AnimatedContent(
                        downloadState.status,
                        Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                        transitionSpec = {
                            if (initialState < targetState) {
                                slideInVertically { height -> height } togetherWith
                                        slideOutVertically { height -> -height }
                            } else {
                                slideInVertically { height -> -height } togetherWith
                                        slideOutVertically { height -> height }
                            }
                        }
                    ) { status ->
                        when(status) {
                            Status.IDLE -> {
                                Row(
                                    Modifier.fillMaxWidth(),
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
                            Status.DOWNLOADING -> {
                                LinearWavyProgressIndicator({
                                    downloadState.progress
                                }, Modifier.fillMaxWidth())
                            }
                            Status.SUCCESS -> {
                                Row(Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        "Download complete",
                                        Modifier.weight(1f)
                                    )
                                    ExpressiveButton(
                                        ButtonDefaults.MediumContainerHeight,
                                        "Close",
                                        colors = ButtonDefaults.filledTonalButtonColors(),
                                        icon = Icons.Default.Close
                                    ) {
                                        viewModel.unselectPicture()
                                    }
                                }
                            }
                            Status.ERROR -> {
                                Row(Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        "Error: ${downloadState.errorMessage ?: "Unknown error"}",
                                        Modifier.weight(1f)
                                    )
                                    ExpressiveButton(
                                        ButtonDefaults.MediumContainerHeight,
                                        "Close",
                                        colors = ButtonDefaults.filledTonalButtonColors(),
                                        icon = Icons.Default.Close
                                    ) {
                                        viewModel.unselectPicture()
                                    }
                                }
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
}