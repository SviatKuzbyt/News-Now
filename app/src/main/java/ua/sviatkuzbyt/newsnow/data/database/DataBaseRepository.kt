package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.room.NewsDataBase
import ua.sviatkuzbyt.newsnow.data.database.room.SavedNewsEntity
import ua.sviatkuzbyt.newsnow.ui.SharedData

class DataBaseRepository(context: Context) {
    private val dao = NewsDataBase.getInstance(context).dao()

    fun getSavedNews(): MutableList<SavedNewsEntity>{
        SharedData.isChangeSaved = false
        return dao.getSavedNews()
    }

    fun add(item: NewsList){
        SharedData.isChangeSaved = true
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
        SharedData.isChangeSaved = true
        dao.deleteSaveNews(item)
    }

    fun isSaved(link: String) = dao.isSaved(link)
}