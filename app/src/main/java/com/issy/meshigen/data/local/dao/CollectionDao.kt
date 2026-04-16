package com.issy.meshigen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.issy.meshigen.data.local.entity.GourmetCollectionEntity
import com.issy.meshigen.data.local.query.CollectionWithGourmetRow
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: GourmetCollectionEntity): Long

    // 全件取得
    @Query(
        """
        SELECT
            gc.id AS collection_id,
            gc.gourmet_id,
            g.name,
            g.area,
            g.category,
            g.description,
            g.search_keyword,
            gc.mood_text,
            gc.ai_comment,
            gc.is_favorite,
            gc.created_at
        FROM gourmet_collection gc
        INNER JOIN gourmets g ON gc.gourmet_id = g.id
        ORDER BY gc.created_at DESC
        """
    )
    fun getAllWithGourmet(): Flow<List<CollectionWithGourmetRow>>

    // コレクションIDで取得
    @Query(
        """
        SELECT
            gc.id AS collection_id,
            gc.gourmet_id,
            g.name,
            g.area,
            g.category,
            g.description,
            g.search_keyword,
            gc.mood_text,
            gc.ai_comment,
            gc.is_favorite,
            gc.created_at
        FROM gourmet_collection gc
        INNER JOIN gourmets g ON gc.gourmet_id = g.id
        WHERE gc.id = :collectionId
        LIMIT 1
        """
    )
    suspend fun getByCollectionId(collectionId: Int): CollectionWithGourmetRow?

    // 削除
    @Query("DELETE FROM gourmet_collection WHERE id = :collectionId")
    suspend fun deleteByCollectionId(collectionId: Int): Int

    // お気に入りの更新
    @Query(
        """
        UPDATE gourmet_collection
        SET is_favorite = :favorite
        WHERE id = :collectionId
        """
    )
    suspend fun updateFavorite(collectionId: Int, favorite: Boolean): Int
}