package org.example.project.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyImage(
    model: Any,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    alt: String? = null,
    onClick: (() -> Unit)? = {}
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = alt,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .then(onClick?.let { Modifier.clickable(onClick = it) } ?: Modifier)
            .size(300.dp),
        loading = {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularWavyProgressIndicator(Modifier.size(48.dp))
            }
        },
        error = {
            Text(alt ?: "Error loading image")
        }
    )
}