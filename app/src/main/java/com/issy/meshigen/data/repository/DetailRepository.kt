package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.query.CollectionWithGourmetRow

interface DetailRepository {
    suspend fun getByGourmetId(gourmetId: Int): CollectionWithGourmetRow?
    suspend fun updateFavoriteByGourmetId(gourmetId: Int, favorite: Boolean): Boolean
    suspend fun deleteByGourmetId(gourmetId: Int): Boolean
}