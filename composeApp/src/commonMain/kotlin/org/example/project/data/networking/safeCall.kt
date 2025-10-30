package org.example.project.data.networking

import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import org.example.project.util.Result
import org.example.project.util.NetworkError

suspend inline fun <reified T> safeCall(
    call: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        call()
    } catch (_: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (_: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION_ERROR)
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        e.printStackTrace()
        return Result.Error(NetworkError.UNKNOWN)
    }
    return responseToResult(response)
}