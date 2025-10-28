package org.example.project.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)