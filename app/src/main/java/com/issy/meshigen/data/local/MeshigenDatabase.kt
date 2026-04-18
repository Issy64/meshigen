package com.issy.meshigen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.issy.meshigen.data.local.dao.CollectionDao
import com.issy.meshigen.data.local.dao.GourmetDao
import com.issy.meshigen.data.local.entity.GourmetCollectionEntity
import com.issy.meshigen.data.local.entity.GourmetEntity

@Database(
    entities = [GourmetEntity::class, GourmetCollectionEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class MeshigenDatabase : RoomDatabase() {
    abstract fun gourmetDao(): GourmetDao
    abstract fun CollectionDao(): CollectionDao

    companion object {
        const val DB_NAME = "meshigen.db"
    }
}
