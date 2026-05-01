package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.dao.CollectionDao
import com.issy.meshigen.data.local.dao.GourmetDao
import com.issy.meshigen.data.local.entity.GourmetCollectionEntity
import com.issy.meshigen.data.local.entity.GourmetEntity

private const val INSERT_IGNORED = -1L

class HomeRepositoryImpl(
    private val gourmetDao: GourmetDao,
    private val collectionDao: CollectionDao,
    private val temporaryAiComment: String,
) : HomeRepository {

    override suspend fun getRecommendation(moodText: String): HomeRecommendation? {
        val gourmets = gourmetDao.getAll()
        val selectedGourmet = selectRecommendation(gourmets) ?: return null

        val collectionItem = GourmetCollectionEntity(
            gourmetId = selectedGourmet.id,
            moodText = moodText,
            aiComment = temporaryAiComment,
        )

        val insertResult = collectionDao.insert(collectionItem)
        val isNewDiscovery = insertResult != INSERT_IGNORED

        return toHomeRecommendation(
            gourmet = selectedGourmet,
            isNewDiscovery = isNewDiscovery,
        )
    }

    private fun toHomeRecommendation(
        gourmet: GourmetEntity,
        isNewDiscovery: Boolean,
    ): HomeRecommendation {
        return HomeRecommendation(
            id = gourmet.id,
            name = gourmet.name,
            description = gourmet.description,
            category = gourmet.category,
            isNewDiscovery = isNewDiscovery,
        )
    }

    private fun selectRecommendation(
        gourmets: List<GourmetEntity>
    ): GourmetEntity? {
        return gourmets.firstOrNull()
    }
}
