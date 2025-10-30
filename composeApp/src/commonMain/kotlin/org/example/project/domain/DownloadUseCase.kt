package org.example.project.domain

expect class DownloadUseCase {
    suspend operator fun invoke(
        url: String,
        path: String,
        onUpdate: (Float) -> Unit,
        onError: (Exception) -> Unit
    )
}