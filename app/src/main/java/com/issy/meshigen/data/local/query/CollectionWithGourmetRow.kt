package com.issy.meshigen.data.local.query

import androidx.room.ColumnInfo

data class CollectionWithGourmetRow(
    @ColumnInfo(name = "collection_id")
    val collectionId: Int,
    @ColumnInfo(name = "gourmet_id")
    val gourmetId: Int,
    val name: String,
    val area: String,
    val category: String,
    val description: String,
    @ColumnInfo(name = "search_keyword")
    val searchKeyword: String,
    @ColumnInfo(name = "mood_text")
    val moodText: String,
    @ColumnInfo(name = "ai_comment")
    val aiComment: String,
    @ColumnInfo(name = "is_favorite")
    val favorite: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
