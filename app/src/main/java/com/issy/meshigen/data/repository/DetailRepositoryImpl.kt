package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.dao.CollectionDao

class DetailRepositoryImpl(
    private val collectionDao: CollectionDao
) : DetailRepository {

    override suspend fun getByGourmetId(gourmetId: Int) =
        collectionDao.getByGourmetId(gourmetId)

    override suspend fun updateFavoriteByGourmetId(gourmetId: Int, favorite: Boolean): Boolean =
        collectionDao.updateFavoriteByGourmetId(gourmetId, favorite) > 0

    override suspend fun deleteByGourmetId(gourmetId: Int): Boolean =
        collectionDao.deleteByGourmetId(gourmetId) > 0

    override suspend fun existsGourmet(gourmetId: Int): Boolean =
        collectionDao.existsGourmet(gourmetId)
}