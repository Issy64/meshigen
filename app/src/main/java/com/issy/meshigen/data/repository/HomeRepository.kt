package com.issy.meshigen.data.repository

interface HomeRepository {
    suspend fun getRecommendation(moodText: String): HomeRecommendation?
}

data class HomeRecommendation(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val isNewDiscovery: Boolean,
)
