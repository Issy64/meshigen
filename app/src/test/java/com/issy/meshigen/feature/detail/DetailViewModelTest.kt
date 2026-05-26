package com.issy.meshigen.feature.detail

import com.issy.meshigen.data.local.query.CollectionWithGourmetRow
import com.issy.meshigen.data.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

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
    fun load_whenIdIsInvalid_stateBecomesNotFound() = runTest {
        val viewModel = DetailViewModel(repository = FakeDetailRepository())

        viewModel.load(gourmetId = "abc")
        val state = viewModel.uiState.first { it !is DetailUiState.Loading }

        assertEquals(DetailUiState.NotFound, state)
    }

    @Test
    fun load_whenCollectionExists_stateBecomesReady() = runTest {
        val row = buildRow(gourmetId = 1)
        val viewModel = DetailViewModel(
            repository = FakeDetailRepository(rowByGourmetId = mapOf(1 to row)),
        )

        viewModel.load(gourmetId = "1")
        val state = viewModel.uiState.first { it !is DetailUiState.Loading }

        assertTrue(state is DetailUiState.Ready)
        val ready = state as DetailUiState.Ready
        assertEquals("name-1", ready.item.name)
        assertEquals("mood-1", ready.item.moodText)
    }

    @Test
    fun load_whenCollectionNotExistsButGourmetExists_stateBecomesLocked() = runTest {
        val viewModel = DetailViewModel(
            repository = FakeDetailRepository(
                rowByGourmetId = emptyMap(),
                existingGourmetIds = setOf(5),
            ),
        )

        viewModel.load(gourmetId = "5")
        val state = viewModel.uiState.first { it !is DetailUiState.Loading }

        assertEquals(DetailUiState.Locked, state)
    }

    @Test
    fun load_whenGourmetDoesNotExist_stateBecomesNotFound() = runTest {
        val viewModel = DetailViewModel(
            repository = FakeDetailRepository(
                rowByGourmetId = emptyMap(),
                existingGourmetIds = emptySet(),
            ),
        )

        viewModel.load(gourmetId = "99")
        val state = viewModel.uiState.first { it !is DetailUiState.Loading }

        assertEquals(DetailUiState.NotFound, state)
    }

    private fun buildRow(gourmetId: Int): CollectionWithGourmetRow = CollectionWithGourmetRow(
        collectionId = gourmetId * 100,
        gourmetId = gourmetId,
        name = "name-$gourmetId",
        area = "area-$gourmetId",
        category = "category-$gourmetId",
        description = "description-$gourmetId",
        searchKeyword = "kw-$gourmetId",
        moodText = "mood-$gourmetId",
        aiComment = "ai-$gourmetId",
        favorite = false,
        createdAt = 1_700_000_000_000L,
    )

    private class FakeDetailRepository(
        private val rowByGourmetId: Map<Int, CollectionWithGourmetRow> = emptyMap(),
        private val existingGourmetIds: Set<Int> = emptySet(),
    ) : DetailRepository {

        override suspend fun getByGourmetId(gourmetId: Int): CollectionWithGourmetRow? =
            rowByGourmetId[gourmetId]

        override suspend fun updateFavoriteByGourmetId(gourmetId: Int, favorite: Boolean): Boolean
                = true

        override suspend fun deleteByGourmetId(gourmetId: Int): Boolean = true

        override suspend fun existsGourmet(gourmetId: Int): Boolean =
            gourmetId in existingGourmetIds
    }
}