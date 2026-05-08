package com.issy.meshigen.feature.home

data class HomeUiState(
    val moodText: String = "",
    val recommendationUiState: HomeRecommendationUiState = HomeRecommendationUiState.Initial,
)

data class RecommendationUiModel(
    val gourmetId: String,
    val name: String,
    val category: String,
    val area: String,
    val comment: String,
    val isNewDiscovery: Boolean,
)

sealed interface HomeRecommendationUiState {
    data object Initial : HomeRecommendationUiState
    data class Success(
        val recommendation: RecommendationUiModel,
    ) : HomeRecommendationUiState
}

sealed interface HomeUiEvent {
    data class MoodTextChanged(val moodText: String) : HomeUiEvent
    data object RecommendClicked : HomeUiEvent
}
