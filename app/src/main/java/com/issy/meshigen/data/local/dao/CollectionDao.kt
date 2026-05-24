package com.issy.meshigen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.issy.meshigen.data.local.entity.GourmetCollectionEntity
import com.issy.meshigen.data.local.query.CollectionWithGourmetRow
import com.issy.meshigen.data.local.query.GourmetWithDiscoveryRow
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

    // gourmets基準で全件取得(未発見を含む)
    @Query(
        """
            SELECT
                g.id AS gourmet_id,
                g.name,
                g.area,
                g.category,
                g.description,
                g.search_keyword,
                (gc.id IS NOT NULL) AS discovered,
                gc.id AS collection_id,
                gc.mood_text,
                gc.ai_comment,
                COALESCE(gc.is_favorite, 0) AS is_favorite,
                gc.created_at
            FROM gourmets AS g
            LEFT JOIN gourmet_collection AS gc ON gc.gourmet_id = g.id
            ORDER BY
                (gc.id IS NULL) ASC,
                gc.created_at DESC,
                g.id ASC
        """
    )
    fun observeAllGourmetsWithDiscovery(): Flow<List<GourmetWithDiscoveryRow>>

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
        WHERE gc.gourmet_id = :gourmetId
        LIMIT 1
        """
    )
    suspend fun getByGourmetId(gourmetId: Int): CollectionWithGourmetRow?

    // 削除
    @Query(
        """
        DELETE
        FROM gourmet_collection
        WHERE gourmet_id = :gourmetId
    """
    )
    suspend fun deleteByGourmetId(gourmetId: Int): Int

    // お気に入りの更新
    @Query(
        """
        UPDATE gourmet_collection
        SET is_favorite = :favorite
        WHERE gourmet_id = :gourmetId
        """
    )
    suspend fun updateFavoriteByGourmetId(gourmetId: Int, favorite: Boolean): Int

    // 件数が1件以上あるかを確認
    @Query(
        """
            SELECT COUNT(*) > 0
            FROM gourmets
            WHERE id = :gourmetId
        """
    )
    suspend fun existsGourmet(gourmetId: Int): Boolean
}
