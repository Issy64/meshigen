package com.issy.meshigen.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var instance: MeshigenDatabase? = null

    fun get(context: Context): MeshigenDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                MeshigenDatabase::class.java,
                MeshigenDatabase.DB_NAME
            ).build().also { db ->
                instance = db
            }
        }
    }
}
