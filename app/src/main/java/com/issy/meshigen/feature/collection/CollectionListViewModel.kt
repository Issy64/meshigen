package com.issy.meshigen.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issy.meshigen.data.repository.CollectionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CollectionListUiState(
    val items: List<CollectionListUiModel> = emptyList(),
)

class CollectionListViewModel(
    private val repository: CollectionRepository,
) : ViewModel() {

    val uiState: StateFlow<CollectionListUiState> =
        repository.observeCollections()
            .map { items -> CollectionListUiState(items = items) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CollectionListUiState(),
            )

    fun onFavoriteClick(item: CollectionListUiModel) {
        val gourmetId = item.gourmetId.toIntOrNull() ?: return
        viewModelScope.launch {
            repository.updateFavorite(gourmetId, !item.favorite)
        }
    }
}
