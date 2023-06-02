package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import ua.sviatkuzbyt.newsnow.changeSaves
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.room.NewsDataBase
import ua.sviatkuzbyt.newsnow.data.database.room.SavedNewsEntity

class DataBaseRepository(context: Context) {
    private val dao = NewsDataBase.getInstance(context).dao()

    fun getSavedNews(): MutableList<SavedNewsEntity>{
        changeSaves = false
        return dao.getSavedNews()
    }

    fun addSavedNews(item: NewsList){
        changeSaves = true
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

    fun remove(item: String){
        changeSaves = true
        dao.deleteSaveNews(item)
    }

    fun isSaved(link: String) = dao.isSaved(link)
}