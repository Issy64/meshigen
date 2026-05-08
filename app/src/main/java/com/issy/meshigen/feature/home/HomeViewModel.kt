package com.issy.meshigen.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issy.meshigen.data.repository.HomeRecommendation
import com.issy.meshigen.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository,
) : ViewModel() {

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
        val moodText = _uiState.value.moodText.trim()
        if (moodText.isBlank()) {
            return
        }

        viewModelScope.launch {
            val recommendation = repository.getRecommendation(moodText) ?: return@launch
            _uiState.update { currentState ->
                currentState.copy(
                    recommendationUiState = HomeRecommendationUiState.Success(
                        recommendation = recommendation.toUiModel(),
                    )
                )
            }
        }
    }

    private fun HomeRecommendation.toUiModel(): RecommendationUiModel {
        return RecommendationUiModel(
            gourmetId = id.toString(),
            name = name,
            category = category,
            area = area,
            comment = comment,
            isNewDiscovery = isNewDiscovery,
        )
    }
}
