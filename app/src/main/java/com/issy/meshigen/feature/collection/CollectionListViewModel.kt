package com.issy.meshigen.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import com.issy.meshigen.data.repository.CollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

data class CollectionListUiState(
    val items: List<CollectionListUiModel> = emptyList(),
    val selectedFilter: String = "all",
    val discoveredCount: Int = 0,
)

class CollectionListViewModel(
    private val repository: CollectionRepository,
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("all")

    val uiState: StateFlow<CollectionListUiState> =
        combine(
            repository.observeAllGourmetsWithDiscovery(),
            _selectedFilter,
        ) { rows, filter ->
            val allItems = rows.map(::toUiModel)
            val filteredItems = when (filter) {
                "undiscovered" -> allItems.filter { !it.discovered }
                "discovered" -> allItems.filter { it.discovered }
                "favorite" -> allItems.filter { it.discovered && it.favorite }
                else -> allItems
            }
            CollectionListUiState(
                items = filteredItems,
                selectedFilter = filter,
                discoveredCount = allItems.count { it.discovered }
            )
        }
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

    fun onFilterClick(filterId: String) {
        _selectedFilter.value = filterId
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
            discovered = row.discovered,
            favorite = row.favorite,
        )
    }
}
