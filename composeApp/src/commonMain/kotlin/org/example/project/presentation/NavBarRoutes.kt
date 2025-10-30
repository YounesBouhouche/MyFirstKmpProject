package org.example.project.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavBarRoutes(
    val icon: ImageVector,
    val label: String,
    val destination: Routes
) {
    HOME(
        icon = Icons.Default.Home,
        label = "Home",
        destination = Routes.Home
    ),
    Search(
        icon = Icons.Default.Search,
        label = "Search",
        destination = Routes.Search
    ),
    History(
        icon = Icons.Default.History,
        label = "History",
        destination = Routes.History
    ),
}