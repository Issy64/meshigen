package com.issy.meshigen.data.repository

import com.issy.meshigen.feature.collection.CollectionListUiModel
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun observeCollections(): Flow<List<CollectionListUiModel>>
    suspend fun updateFavorite(collectionId: Int, favorite: Boolean)
}