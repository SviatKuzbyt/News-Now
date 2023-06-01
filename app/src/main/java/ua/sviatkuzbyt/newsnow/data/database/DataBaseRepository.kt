package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.room.NewsDataBase
import ua.sviatkuzbyt.newsnow.data.database.room.SavedNewsEntity

class DataBaseRepository(context: Context) {
    private val dao = NewsDataBase.getInstance(context).dao()

    fun getSavedNews() = dao.getSavedNews()

    fun addSavedNews(item: NewsList){
        dao.addSaveNews(
            SavedNewsEntity(
                0,
                item.label,
                item.source,
                item.time,
                item.link
            )
        )
    }

    fun removeSavedNews(item: String){
        dao.deleteSaveNews(item)
    }

    fun isSaved(link: String) = dao.isSaved(link)
}