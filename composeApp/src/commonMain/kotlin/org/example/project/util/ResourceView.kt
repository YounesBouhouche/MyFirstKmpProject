package org.example.project.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.presentation.util.ExpressiveButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun<D> ResourceView(
    resource: Resource<D, NetworkError>,
    modifier: Modifier = Modifier,
    idleContent: @Composable () -> Unit = {},
    onCancel: (() -> Unit)? = null,
    content: @Composable (D) -> Unit,
) {
    AnimatedContent(resource, modifier.fillMaxSize()) {
        when(it) {
            is Resource.Error<*> -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("An error occurred: ${it.error}")
                    onCancel?.let { cancel ->
                        Button(onClick = cancel) {
                            Text("Cancel")
                        }
                    }
                }
            }
            Resource.Idle -> {
                idleContent()
            }
            Resource.Loading -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularWavyProgressIndicator(Modifier.size(100.dp))
                    Text("Loading...")
                    onCancel?.let { cancel ->
                        ExpressiveButton(
                            text = "Cancel",
                            size = ButtonDefaults.MediumContainerHeight,
                            onClick = cancel
                        )
                    }
                }
            }
            is Resource.Success -> {
                content(it.data)
            }
        }
    }
}