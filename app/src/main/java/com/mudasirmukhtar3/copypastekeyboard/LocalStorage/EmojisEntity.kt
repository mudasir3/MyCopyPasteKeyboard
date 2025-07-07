package com.sharpforks.copypaste.LocalStorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Emojis(
    @PrimaryKey(autoGenerate = true) var pid: Int = 0,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "emoji_order") var eOrder:Int
)