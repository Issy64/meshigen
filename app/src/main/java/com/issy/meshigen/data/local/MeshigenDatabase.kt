package com.issy.meshigen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.issy.meshigen.data.local.dao.GourmetDao
import com.issy.meshigen.data.local.entity.GourmetEntity

@Database(
    entities = [GourmetEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class MeshigenDatabase : RoomDatabase() {
    abstract fun gourmetDao(): GourmetDao

    companion object {
        const val DB_NAME = "meshigen.db"
    }
}
