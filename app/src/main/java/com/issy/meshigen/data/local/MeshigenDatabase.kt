package com.issy.meshigen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RoomPlaceholderEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class MeshigenDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "meshigen.db"
    }
}
