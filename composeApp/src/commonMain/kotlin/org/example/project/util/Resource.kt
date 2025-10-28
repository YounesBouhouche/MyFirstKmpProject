package org.example.project.util

sealed class Resource<out D, out E> {
    data object Idle: Resource<Nothing, Nothing>()
    data object Loading: Resource<Nothing, Nothing>()
    data class Success<out D>(val data: D): Resource<D, Nothing>()
    data class Error<out E>(val error: E): Resource<Nothing, E>()
}