package com.issy.meshigen.data.repository

import com.issy.meshigen.data.local.dao.CollectionDao
import com.issy.meshigen.data.local.dao.GourmetDao
import com.issy.meshigen.data.local.entity.GourmetCollectionEntity
import com.issy.meshigen.data.local.entity.GourmetEntity
import com.issy.meshigen.data.local.query.CollectionWithGourmetRow
import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HomeRepositoryImplTest {

    @Test
    fun getRecommendation_whenInsertSucceeds_returnsNewDiscoveryTrue() = runBlocking {
        val repository = HomeRepositoryImpl(
            gourmetDao = FakeGourmetDao(listOf(testGourmet)),
            collectionDao = FakeCollectionDao(insertResult = 1L),
            temporaryAiComment = temporaryAiComment,
        )

        val result = repository.getRecommendation(moodText = moodText)

        assertNotNull(result)
        assertEquals(true, result?.isNewDiscovery)
    }

    @Test
    fun getRecommendation_whenInsertIgnored_returnsNewDiscoveryFalse() = runBlocking {
        val repository = HomeRepositoryImpl(
            gourmetDao = FakeGourmetDao(listOf(testGourmet)),
            collectionDao = FakeCollectionDao(insertResult = -1L),
            temporaryAiComment = temporaryAiComment,
        )

        val result = repository.getRecommendation(moodText = moodText)

        assertNotNull(result)
        assertEquals(false, result?.isNewDiscovery)
    }

    @Test
    fun getRecommendation_returnsSelectedGourmetWithTemporaryComment() = runBlocking {
        val repository = HomeRepositoryImpl(
            gourmetDao = FakeGourmetDao(listOf(testGourmet)),
            collectionDao = FakeCollectionDao(insertResult = 1L),
            temporaryAiComment = temporaryAiComment,
        )

        val result = repository.getRecommendation(moodText = moodText)

        assertNotNull(result)
        assertEquals(testGourmet.id, result?.id)
        assertEquals(testGourmet.name, result?.name)
        assertEquals(testGourmet.category, result?.category)
        assertEquals(testGourmet.area, result?.area)
        assertEquals(temporaryAiComment, result?.comment)
    }

    @Test
    fun getRecommendation_whenGourmetsAreEmpty_returnsNull() = runBlocking {
        val collectionDao = FakeCollectionDao(insertResult = 1L)
        val repository = HomeRepositoryImpl(
            gourmetDao = FakeGourmetDao(emptyList()),
            collectionDao = collectionDao,
            temporaryAiComment = temporaryAiComment,
        )

        val result = repository.getRecommendation(moodText = moodText)

        assertNull(result)
        assertNull(collectionDao.insertedItem)
    }

    @Test
    fun getRecommendation_savesSelectedGourmetWithMoodAndAiComment() = runBlocking {
        val collectionDao = FakeCollectionDao(insertResult = 1L)
        val repository = HomeRepositoryImpl(
            gourmetDao = FakeGourmetDao(listOf(testGourmet)),
            collectionDao = collectionDao,
            temporaryAiComment = temporaryAiComment,
        )

        repository.getRecommendation(moodText = moodText)

        val insertedItem = collectionDao.insertedItem
        assertNotNull(insertedItem)
        assertEquals(testGourmet.id, insertedItem?.gourmetId)
        assertEquals(moodText, insertedItem?.moodText)
        assertEquals(temporaryAiComment, insertedItem?.aiComment)
    }

    private class FakeGourmetDao(
        private val gourmets: List<GourmetEntity>,
    ) : GourmetDao {

        override suspend fun insertAll(items: List<GourmetEntity>) = Unit

        override suspend fun getById(id: Int): GourmetEntity? {
            return gourmets.firstOrNull { gourmet -> gourmet.id == id }
        }

        override suspend fun getAll(): List<GourmetEntity> = gourmets

        override suspend fun count(): Int = gourmets.size
    }

    private class FakeCollectionDao(
        private val insertResult: Long,
    ) : CollectionDao {

        var insertedItem: GourmetCollectionEntity? = null
            private set

        override suspend fun insert(item: GourmetCollectionEntity): Long {
            insertedItem = item
            return insertResult
        }

        override fun getAllWithGourmet(): Flow<List<CollectionWithGourmetRow>> = emptyFlow()

        override fun observeAllGourmetsWithDiscovery(): Flow<List<GourmetWithDiscoveryRow>> = emptyFlow()

        override suspend fun getByGourmetId(gourmetId: Int): CollectionWithGourmetRow? = null

        override suspend fun deleteByGourmetId(gourmetId: Int): Int = 0

        override suspend fun updateFavoriteByGourmetId(gourmetId: Int, favorite: Boolean): Int = 0

        override suspend fun existsGourmet(gourmetId: Int): Boolean = false
    }

    private companion object {
        const val moodText = "こってりしたものが食べたい"
        const val temporaryAiComment = "仮の紹介文"

        val testGourmet = GourmetEntity(
            id = 1,
            name = "焼きカレー",
            area = "門司港",
            category = "ごはん",
            description = "香ばしく焼いたカレーです。",
            searchKeyword = "北九州 焼きカレー",
        )
    }
}
