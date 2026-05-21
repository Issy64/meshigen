package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun observeAllGourmetsWithDiscovery(): Flow<List<GourmetWithDiscoveryRow>>
    suspend fun updateFavorite(gourmetId: Int, favorite: Boolean)
}