package com.issy.meshigen.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.issy.meshigen.data.local.seed.GourmetSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseProvider {

    @Volatile
    private var instance: MeshigenDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS gourmet_collection (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    gourmet_id INTEGER NOT NULL,
                    mood_text TEXT NOT NULL,
                    ai_comment TEXT NOT NULL,
                    is_favorite INTEGER NOT NULL DEFAULT 0,
                    created_at INTEGER NOT NULL,
                    FOREIGN KEY(gourmet_id) REFERENCES gourmets(id)
                )
                """.trimIndent()
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS index_gourmet_collection_gourmet_id ON gourmet_collection(gourmet_id)"
            )
        }
    }

    fun get(context: Context): MeshigenDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                MeshigenDatabase::class.java,
                MeshigenDatabase.DB_NAME
            ).addMigrations(MIGRATION_1_2)
                .build().also { db ->
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
