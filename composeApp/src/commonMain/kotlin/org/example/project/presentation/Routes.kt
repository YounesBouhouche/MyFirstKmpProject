package org.example.project.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Home: Routes()

    @Serializable
    data object Search: Routes()

    @Serializable
    data object History: Routes()
}

