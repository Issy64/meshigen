package com.issy.meshigen.data.local

import android.content.Context
import androidx.room.Room
import com.issy.meshigen.data.local.seed.GourmetSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

                CoroutineScope(Dispatchers.IO).launch {
                    val dao = db.gourmetDao()
                    if (dao.count() == 0) {
                        dao.insertAll(GourmetSeedData.items)
                    }
                }
            }
        }
    }
}
