package com.issy.meshigen.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gourmet_collection",
    foreignKeys = [
        ForeignKey(
            entity = GourmetEntity::class,
            parentColumns = ["id"],
            childColumns = ["gourmet_id"],
        )
    ],
    indices = [Index(value = ["gourmet_id"])]
)
data class GourmetCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "gourmet_id")
    val gourmetId: Int,
    @ColumnInfo(name = "mood_text")
    val moodText: String,
    @ColumnInfo(name = "ai_comment")
    val aiComment: String,
    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    val favorite: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
