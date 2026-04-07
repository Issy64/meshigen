package com.issy.meshigen.feature.home

internal object HomeDummyDataSource {
    fun createDummyRecommendations(): List<RecommendationUiModel> = listOf(
        RecommendationUiModel(
            id = "kokura_yaki_udon",
            name = "小倉焼うどん",
            description = "香ばしくて満足感があり、ガッツリ食べたい気分に合います。",
            category = "麺"
        ),
        RecommendationUiModel(
            id = "yaki_curry",
            name = "焼きカレー",
            description = "熱々で濃厚なので、ちょっと元気を出したい時に向いています。",
            category = "ご飯もの"
        )
    )
}
