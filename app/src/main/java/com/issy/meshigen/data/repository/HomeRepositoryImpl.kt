package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.dao.GourmetDao
import com.issy.meshigen.data.local.entity.GourmetEntity

class HomeRepositoryImpl(
    private val gourmetDao: GourmetDao
) : HomeRepository {

    override suspend fun getRecommendation(): HomeRecommendation? {
        val gourmets = gourmetDao.getAll()
        val selectedGourmet = selectRecommendation(gourmets) ?: return null

        return toHomeRecommendation(selectedGourmet)
    }

    private fun toHomeRecommendation(
        gourmet: GourmetEntity
    ): HomeRecommendation {
        return HomeRecommendation(
            id = gourmet.id,
            name = gourmet.name,
            description = gourmet.description,
            category = gourmet.category,
        )
    }

    private fun selectRecommendation(
        gourmets: List<GourmetEntity>
    ): GourmetEntity? {
        return gourmets.firstOrNull()
    }
}
