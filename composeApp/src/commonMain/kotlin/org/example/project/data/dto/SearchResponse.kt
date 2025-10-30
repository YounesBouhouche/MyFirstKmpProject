package org.example.project.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val next_page: String? = null,
    val prev_page: String? = null,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)