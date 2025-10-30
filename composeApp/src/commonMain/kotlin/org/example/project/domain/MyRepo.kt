package org.example.project.domain

import org.example.project.data.dto.SearchResponse
import org.example.project.data.networking.PexelsClient
import org.example.project.util.NetworkError
import org.example.project.util.Result

interface MyRepo {
    fun helloWorld(): String
    suspend fun searchPictures(query: String): Result<SearchResponse, NetworkError>
}

class MyRepoImpl(
    private val dbClient: DbClient,
    private val pexelsClient: PexelsClient
): MyRepo {
    override fun helloWorld(): String {
        return "Hello world!"
    }

    override suspend fun searchPictures(query: String): Result<SearchResponse, NetworkError> {
        return pexelsClient.searchPhotos(query)
    }
}