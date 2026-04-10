package com.issy.meshigen.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.MoodTextChanged -> updateMoodText(event.moodText)
            HomeUiEvent.RecommendClicked -> recommendIfPossible()
        }
    }

    private fun updateMoodText(moodText: String) {
        _uiState.update { currentState ->
            currentState.copy(moodText = moodText)
        }
    }

    private fun recommendIfPossible() {
        if (_uiState.value.moodText.isBlank()) {
            return
        }

        val items = HomeDummyDataSource.createDummyRecommendations()
        _uiState.update { currentState ->
            currentState.copy(
                recommendationUiState = HomeRecommendationUiState.Success(items)
            )
        }
    }
}
