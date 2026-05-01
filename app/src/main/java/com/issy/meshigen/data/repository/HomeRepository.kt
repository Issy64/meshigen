package com.issy.meshigen.data.repository

interface HomeRepository {
    suspend fun getRecommendation(): HomeRecommendation?
}

data class HomeRecommendation(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
)
