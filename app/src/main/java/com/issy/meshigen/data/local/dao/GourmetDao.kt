package com.issy.meshigen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.issy.meshigen.data.local.entity.GourmetEntity

@Dao
interface GourmetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<GourmetEntity>)

    @Query("SELECT * FROM gourmets WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): GourmetEntity?

    @Query("SELECT * FROM gourmets ORDER BY id ASC")
    suspend fun getAll(): List<GourmetEntity>

    @Query("SELECT COUNT(*) FROM gourmets")
    suspend fun count(): Int
}