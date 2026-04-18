package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.dao.CollectionDao
import com.issy.meshigen.data.local.query.CollectionWithGourmetRow
import com.issy.meshigen.feature.collection.CollectionListUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId

class CollectionRepositoryImpl(
    private val collectionDao: CollectionDao
): CollectionRepository {

    override fun observeCollections(): Flow<List<CollectionListUiModel>> {
        return collectionDao.getAllWithGourmet()
            .map { rows -> rows.map(::toUiModel) }
    }

    override suspend fun updateFavorite(gourmetId: Int, favorite: Boolean) {
        collectionDao.updateFavoriteByGourmetId(gourmetId, favorite)
    }

    private fun toUiModel(row: CollectionWithGourmetRow): CollectionListUiModel {
        return CollectionListUiModel(
            collectionId = row.collectionId.toString(),
            gourmetId = row.gourmetId.toString(),
            name = row.name,
            category = row.category,
            area = row.area,
            suggestedDate = Instant.ofEpochMilli(row.createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .toString(),
            favorite = row.favorite,
        )
    }
}