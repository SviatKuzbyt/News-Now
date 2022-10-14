package ua.sviatkuzbyt.newsnow.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RequestsNewsData {
    @Insert
    fun addSaveNews(news: SavedNewsEntity)

//    @Delete
//    fun deleteSaveNews(news: SavedNewsEntity)
//
//    @Query("SELECT * FROM savednews")
//    fun getSavedNews(): SavedNewsEntity

}