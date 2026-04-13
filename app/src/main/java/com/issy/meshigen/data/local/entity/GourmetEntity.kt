package com.issy.meshigen.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gourmets")
data class GourmetEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val area: String,
    val category: String,
    val description: String,
    @ColumnInfo(name = "search_keyword") val searchKeyword: String,
)
