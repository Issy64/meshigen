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

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 既存の非ユニークIndexを削除してから、重複整理後にユニークIndexを作る。
            db.execSQL("DROP INDEX IF EXISTS index_gourmet_collection_gourmet_id")

            // 各 gourmet_id の最古レコードに、重複群の favorite 状態を集約する。
            db.execSQL(
                """
                UPDATE gourmet_collection
                SET is_favorite = 1
                WHERE id IN (
                    SELECT keep_rows.id
                    FROM gourmet_collection keep_rows
                    JOIN (
                        SELECT gourmet_id, MIN(created_at) AS min_created_at
                        FROM gourmet_collection
                        GROUP BY gourmet_id
                    ) first_seen
                      ON keep_rows.gourmet_id = first_seen.gourmet_id
                     AND keep_rows.created_at = first_seen.min_created_at
                    JOIN (
                        SELECT gourmet_id
                        FROM gourmet_collection
                        GROUP BY gourmet_id
                        HAVING MAX(is_favorite) = 1
                    ) any_favorite
                      ON keep_rows.gourmet_id = any_favorite.gourmet_id
                )
                """.trimIndent()
            )

            // 1 gourmet_id につき created_at 最古、同値なら id 最小の1件だけ残す。
            db.execSQL(
                """
                DELETE FROM gourmet_collection
                WHERE id NOT IN (
                    SELECT id
                    FROM (
                        SELECT gc.id
                        FROM gourmet_collection gc
                        WHERE gc.id = (
                            SELECT gc2.id
                            FROM gourmet_collection gc2
                            WHERE gc2.gourmet_id = gc.gourmet_id
                            ORDER BY gc2.created_at ASC, gc2.id ASC
                            LIMIT 1
                        )
                    )
                )
                """.trimIndent()
            )

            db.execSQL(
                "CREATE UNIQUE INDEX IF NOT EXISTS index_gourmet_collection_gourmet_id ON gourmet_collection(gourmet_id)"
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
                .addMigrations(MIGRATION_2_3)
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
