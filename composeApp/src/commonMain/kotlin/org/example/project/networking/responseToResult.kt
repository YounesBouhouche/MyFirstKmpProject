package org.example.project.networking

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import org.example.project.util.NetworkError
import org.example.project.util.Result
import org.example.project.util.Result.*

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Success(response.body<T>())
            } catch (_: NoTransformationFoundException) {
                Error(NetworkError.SERIALIZATION_ERROR)
            }
        }
        400 -> Error(NetworkError.BAD_REQUEST)
        401 -> Error(NetworkError.UNAUTHORIZED)
        403 -> Error(NetworkError.FORBIDDEN)
        404 -> Error(NetworkError.NOT_FOUND)
        408 -> Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Error(NetworkError.SERVER_ERROR)
        else -> Error(NetworkError.UNKNOWN)
    }
}
