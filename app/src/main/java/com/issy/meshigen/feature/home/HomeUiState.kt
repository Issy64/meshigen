package com.issy.meshigen.feature.home

data class HomeUiState(
    val moodText: String = "",
    val recommendationUiState: HomeRecommendationUiState = HomeRecommendationUiState.Initial,
)

data class RecommendationUiModel(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
)

sealed interface HomeRecommendationUiState {
    data object Initial : HomeRecommendationUiState
    data class Success(val items: List<RecommendationUiModel>) : HomeRecommendationUiState
}

sealed interface HomeUiEvent {
    data class MoodTextChanged(val moodText: String) : HomeUiEvent
    data object RecommendClicked : HomeUiEvent
    data object ImeDone : HomeUiEvent
}
