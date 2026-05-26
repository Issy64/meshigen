package com.issy.meshigen.feature.collection

import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import com.issy.meshigen.data.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_initialState_isAllFilterAndAllItems() = runTest {
        val rows = listOf(
            buildRow(gourmetId = 1, discovered = true, favorite = true),
            buildRow(gourmetId = 2, discovered = true, favorite = false),
            buildRow(gourmetId = 3, discovered = false),
        )
        val viewModel = CollectionListViewModel(
            repository = FakeCollectionRepository(rowsToEmit = rows),
        )

        val state = viewModel.uiState.first { it.items.isNotEmpty() }

        assertEquals("all", state.selectedFilter)
        assertEquals(3, state.items.size)
        assertEquals(2, state.discoveredCount)
    }

    @Test
    fun uiState_withUndiscoveredFilter_returnsOnlyUndiscoveredItems() = runTest {
        val rows = listOf(
            buildRow(gourmetId = 1, discovered = true),
            buildRow(gourmetId = 2, discovered = false),
            buildRow(gourmetId = 3, discovered = false),
        )
        val viewModel = CollectionListViewModel(
            repository = FakeCollectionRepository(rowsToEmit = rows),
        )

        viewModel.onFilterClick("undiscovered")
        val state = viewModel.uiState.first { it.selectedFilter == "undiscovered" }

        assertEquals(2, state.items.size)
        assertEquals(0, state.items.count { it.discovered })
    }

    @Test
    fun uiState_withDiscoveredFilter_returnsOnlyDiscoveredItems() = runTest {
        val rows = listOf(
            buildRow(1, discovered = true),
            buildRow(2, discovered = true),
            buildRow(3, discovered = false),
        )
        val viewModel = CollectionListViewModel(
            repository = FakeCollectionRepository(rowsToEmit = rows),
        )

        viewModel.onFilterClick("discovered")
        val state = viewModel.uiState.first { it.selectedFilter == "discovered" }

        assertEquals(2, state.items.size)
        assertEquals(2, state.items.count { it.discovered })
    }

    @Test
    fun uiState_withFavoriteFilter_returnsOnlyDiscoveredAndFavoriteItems() = runTest {
        val rows = listOf(
            buildRow(1, discovered = true, favorite = true),
            buildRow(2, discovered = true, favorite = false),
            buildRow(3, discovered = false, favorite = true),
        )
        val viewModel = CollectionListViewModel(
            repository = FakeCollectionRepository(rowsToEmit = rows),
        )

        viewModel.onFilterClick("favorite")
        val state = viewModel.uiState.first { it.selectedFilter == "favorite" }

        assertEquals(1, state.items.size)
        assertEquals(true, state.items[0].discovered)
        assertEquals(true, state.items[0].favorite)
    }

    @Test
    fun uiState_discoveredCount_remainsTotalDiscoveredRegardlessOfFilter() = runTest {
        val rows = listOf(
            buildRow(1, discovered = true),
            buildRow(2, discovered = true),
            buildRow(3, discovered = false),
        )
        val viewModel = CollectionListViewModel(
            repository = FakeCollectionRepository(rowsToEmit = rows),
        )

        viewModel.onFilterClick("undiscovered")
        val state = viewModel.uiState.first { it.selectedFilter == "undiscovered" }

        assertEquals(2, state.discoveredCount)
    }


    private fun buildRow(
        gourmetId: Int,
        discovered: Boolean = false,
        favorite: Boolean = false,
    ): GourmetWithDiscoveryRow = GourmetWithDiscoveryRow(
        gourmetId = gourmetId,
        name = "name-$gourmetId",
        area = "area-$gourmetId",
        category = "category-$gourmetId",
        description = "description-$gourmetId",
        searchKeyword = "kw-$gourmetId",
        discovered = discovered,
        collectionId = if (discovered) gourmetId else null,
        moodText = if (discovered) "mood-$gourmetId" else null,
        aiComment = if (discovered) "ai-$gourmetId" else null,
        favorite = favorite,
        createdAt = if (discovered) 1_700_000_000_000L else null,
    )

    private class FakeCollectionRepository(
        private val rowsToEmit: List<GourmetWithDiscoveryRow>,
    ) : CollectionRepository {

        var updateFavoriteCalledWith: Pair<Int, Boolean>? = null
            private set

        override fun observeAllGourmetsWithDiscovery() = kotlinx.coroutines.flow.flowOf(rowsToEmit)

        override suspend fun updateFavorite(gourmetId: Int, favorite: Boolean) {
            updateFavoriteCalledWith = gourmetId to favorite
        }
    }
}