package com.issy.meshigen.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import com.issy.meshigen.data.repository.CollectionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

data class CollectionListUiState(
    val items: List<CollectionListUiModel> = emptyList(),
)

class CollectionListViewModel(
    private val repository: CollectionRepository,
) : ViewModel() {

    val uiState: StateFlow<CollectionListUiState> =
        repository.observeAllGourmetsWithDiscovery()
            .map { rows -> CollectionListUiState(items = rows.map(::toUiModel)) }
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

    private fun toUiModel(row: GourmetWithDiscoveryRow): CollectionListUiModel {
        return CollectionListUiModel(
            collectionId = row.collectionId?.toString() ?: "",
            gourmetId = row.gourmetId.toString(),
            name = row.name,
            category = row.category,
            area = row.area,
            suggestedDate = row.createdAt
                ?.let { Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .toString() }
            ?: "",
            favorite = row.favorite,
        )
    }
}
