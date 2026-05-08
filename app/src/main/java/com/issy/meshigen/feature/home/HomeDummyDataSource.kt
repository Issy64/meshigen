package com.issy.meshigen.feature.home

internal object HomeDummyDataSource {
    fun createDummyRecommendation(): RecommendationUiModel {
        return RecommendationUiModel(
            gourmetId = "1",
            name = "小倉焼うどん",
            category = "麺",
            area = "小倉",
            comment = "香ばしくて満足感があり、ガッツリ食べたい気分に合います。",
            isNewDiscovery = true,
        )
    }
}
