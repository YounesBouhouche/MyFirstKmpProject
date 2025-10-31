package org.example.project.presentation

data class DownloadState(
    val status: Status = Status.IDLE,
    val progress: Float = 0f,
    val errorMessage: String? = null
)

enum class Status {
    IDLE,
    DOWNLOADING,
    SUCCESS,
    ERROR
}
