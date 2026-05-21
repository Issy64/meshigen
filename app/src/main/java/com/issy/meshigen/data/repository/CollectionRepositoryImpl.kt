package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.dao.CollectionDao
import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import kotlinx.coroutines.flow.Flow

class CollectionRepositoryImpl(
    private val collectionDao: CollectionDao
): CollectionRepository {

    override fun observeAllGourmetsWithDiscovery(): Flow<List<GourmetWithDiscoveryRow>> {
        return collectionDao.observeAllGourmetsWithDiscovery()
    }

    override suspend fun updateFavorite(gourmetId: Int, favorite: Boolean) {
        collectionDao.updateFavoriteByGourmetId(gourmetId, favorite)
    }
}