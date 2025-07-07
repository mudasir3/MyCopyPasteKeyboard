package com.sharpforks.copypaste.LocalStorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Text(
    @PrimaryKey(autoGenerate = true) var pid: Int = 0,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "text_order") var tOrder:Int

)