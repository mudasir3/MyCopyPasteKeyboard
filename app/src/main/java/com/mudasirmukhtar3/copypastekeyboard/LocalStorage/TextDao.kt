package com.sharpforks.copypaste.LocalStorage

import androidx.room.*


@Dao
interface TextDao {
    @Insert
    fun insert(text: Text)

    @Update
    fun update(text: Text)

    @Query("DELETE FROM Text")
    fun delete()

    @Query("DELETE FROM Emojis")
    fun deleteEmoji()

    @Query("SELECT * FROM Text")
    fun getAll(): List<Text>

    @Insert
    fun insertEmojis(emojis: Emojis)

    @Update
    fun updateEmojis(emojis: Emojis)

    @Query("DELETE FROM EMOJIS")
    fun deleteEmojis()

    @Query("SELECT * FROM Emojis")
    fun getAllEmojis(): List<Emojis>

    @Query("SELECT * FROM Emojis WHERE text = :txt")
    fun checkEmoji(txt:String): List<Emojis>

    @Query("SELECT * FROM Text WHERE text = :txt")
    fun checkText(txt:String): List<Text>

    @Query("SELECT EXISTS(SELECT * FROM Text WHERE text = :txt)")
    fun isTextIsExist(txt: String) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM Emojis WHERE text = :txt)")
    fun isEmojiIsExist(txt: String) : Boolean

    @Query("SELECT MAX(text_order) FROM Text")
    fun getLargestOrder(): Int

    @Query("SELECT MAX(emoji_order) FROM Emojis")
    fun getLargestOrderEmoji(): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTextListOrder(text: ArrayList<Text>)

}