package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.dao.CollectionDao
import com.issy.meshigen.data.local.entity.GourmetCollectionEntity
import com.issy.meshigen.data.local.query.CollectionWithGourmetRow
import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionRepositoryImplTest {

    @Test
    fun observeAllGourmetsWithDiscovery_returnsAll30RowsFromDao() = runBlocking {
        val rows = List(30) { index -> buildRow(gourmetId = index + 1) }
        val repository = CollectionRepositoryImpl(
            collectionDao = FakeCollectionDao(rowsToEmit = rows)
        )

        val result = repository.observeAllGourmetsWithDiscovery().first()

        assertEquals(30, result.size)
    }

    @Test
    fun observeAllGourmetsWithDiscovery_passesThroughDiscoveredRow() = runBlocking {
        val discoveredRow = buildRow(gourmetId = 1, discovered = true, favorite = true)
        val repository = CollectionRepositoryImpl(
            collectionDao = FakeCollectionDao(rowsToEmit = listOf(discoveredRow)),
        )

        val result = repository.observeAllGourmetsWithDiscovery().first()

        assertEquals(1, result.size)
        assertEquals(true, result[0].discovered)
        assertEquals(true, result[0].favorite)
        assertEquals("name-1", result[0].name)
    }

    @Test
    fun observeAllGourmetsWithDiscovery_passesThroughUndiscoveredRow() = runBlocking {
        val undiscoveredRow = buildRow(gourmetId = 2, discovered = false)
        val repository = CollectionRepositoryImpl(
            collectionDao = FakeCollectionDao(rowsToEmit = listOf(undiscoveredRow)),
        )

        val result = repository.observeAllGourmetsWithDiscovery().first()

        assertEquals(1, result.size)
        assertEquals(false, result[0].discovered)
        assertEquals(null, result[0].collectionId)
        assertEquals(null, result[0].createdAt)
    }

    @Test
    fun updateFavorite_delegatesToDaoWithTrue() = runBlocking {
        val dao = FakeCollectionDao()
        val repository = CollectionRepositoryImpl(collectionDao = dao)

        repository.updateFavorite(gourmetId = 5, favorite = true)

        assertEquals(5 to true, dao.updateFavoriteCalledWith)
    }

    @Test
    fun updateFavorite_delegatesToDaoWithFalse() = runBlocking {
        val dao = FakeCollectionDao()
        val repository = CollectionRepositoryImpl(collectionDao = dao)

        repository.updateFavorite(gourmetId = 7, favorite = false)

        assertEquals(7 to false, dao.updateFavoriteCalledWith)
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

    private class FakeCollectionDao(
        private val rowsToEmit: List<GourmetWithDiscoveryRow> = emptyList(),
    ) : CollectionDao {

        var updateFavoriteCalledWith: Pair<Int, Boolean>? = null
            private set

        override suspend fun insert(item: GourmetCollectionEntity): Long = 0L

        override fun getAllWithGourmet(): Flow<List<CollectionWithGourmetRow>> = flowOf(emptyList())

        override fun observeAllGourmetsWithDiscovery(): Flow<List<GourmetWithDiscoveryRow>> = flowOf(rowsToEmit)

        override suspend fun getByGourmetId(gourmetId: Int): CollectionWithGourmetRow? = null

        override suspend fun deleteByGourmetId(gourmetId: Int): Int = 0

        override suspend fun updateFavoriteByGourmetId(gourmetId: Int, favorite: Boolean): Int {
            updateFavoriteCalledWith = gourmetId to favorite
            return 1
        }

        override suspend fun existsGourmet(gourmetId: Int): Boolean = false
    }
}