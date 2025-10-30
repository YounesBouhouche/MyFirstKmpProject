package org.example.project.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.example.project.data.dto.SearchResponse
import org.example.project.util.NetworkError
import org.example.project.util.Result

class PexelsClient(private val httpClient: HttpClient) {
    suspend fun searchPhotos(
        query: String,
        perPage: Int = 15,
        page: Int = 1
    ): Result<SearchResponse, NetworkError> {
        return safeCall<SearchResponse> {
            httpClient.get("https://api.pexels.com/v1/search") {
                parameter("query", query)
                parameter("per_page", perPage)
                parameter("page", page)
            }.body()
        }
    }
}