package ua.sviatkuzbyt.newsnow.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RequestsNewsData {
    @Insert
    fun addSaveNews(news: SavedNewsEntity)

    @Query("DELETE FROM savednews WHERE (((savednews.link)=:news))")
    fun deleteSaveNews(news: String)

    @Query("SELECT * FROM savednews")
    fun getSavedNews(): MutableList<SavedNewsEntity>

    @Query("SELECT COUNT(*) FROM savednews WHERE link IN (:news)")
    fun isSaved(news: String): Boolean

}