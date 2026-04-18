package com.issy.meshigen.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issy.meshigen.data.repository.DetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Ready(val item: DetailUiModel) : DetailUiState
    data object NotFound : DetailUiState
}

class DetailViewModel(
    private val repository: DetailRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun load(gourmetId: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            val id = gourmetId.toIntOrNull()
            if (id == null) {
                _uiState.value = DetailUiState.NotFound
                return@launch
            }

            val row = repository.getByGourmetId(id)
            if (row == null) {
                _uiState.value = DetailUiState.NotFound
                return@launch
            }

            _uiState.value = DetailUiState.Ready(
                DetailUiModel(
                    name = row.name,
                    category = row.category,
                    area = row.area,
                    description = row.description,
                    aiComment = row.aiComment,
                    moodText = row.moodText,
                    suggestedDate = java.time.Instant.ofEpochMilli(row.createdAt)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate()
                        .toString(), // yyyy-MM-dd
                    favorite = row.favorite,
                )
            )
        }
    }
}
