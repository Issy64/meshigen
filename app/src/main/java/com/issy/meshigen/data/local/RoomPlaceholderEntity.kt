package com.issy.meshigen.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_placeholder")
data class RoomPlaceholderEntity(
    @PrimaryKey val id: Int = 1
)
