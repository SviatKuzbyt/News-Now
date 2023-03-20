package ua.sviatkuzbyt.newsnow.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDataBaseDao {
    @Insert
    fun addSaveNews(news: SavedNewsEntity)

    @Query("DELETE FROM savednews WHERE (((savednews.link)=:news))")
    fun deleteSaveNews(news: String)

    @Query("SELECT * FROM savednews ORDER BY id DESC")
    fun getSavedNews(): MutableList<SavedNewsEntity>

    @Query("SELECT COUNT(*) FROM savednews WHERE link IN (:news)")
    fun isSaved(news: String): Boolean

    @Query("SELECT text FROM historySearch ORDER BY id DESC")
    fun getHistory(): MutableList<String>

    @Insert
    fun addHistory(history: HistoryEntity)

    @Query("DELETE FROM historySearch WHERE text=:history")
    fun deleteHistory(history: String)
}