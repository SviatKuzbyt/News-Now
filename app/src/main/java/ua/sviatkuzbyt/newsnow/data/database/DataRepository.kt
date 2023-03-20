package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import ua.sviatkuzbyt.newsnow.data.NewsList

class DataRepository(context: Context) {
    private val dao = NewsDataBase.getInstance(context).dao()

    fun getSavedNews() = dao.getSavedNews()

    fun removeSavedNews(item: String){
        dao.deleteSaveNews(item)
    }

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

    fun getHistory() = dao.getHistory()

    fun deleteHistory(history: String){
        dao.deleteHistory(history)
    }

    fun addHistory(text: String) {
        dao.addHistory(HistoryEntity(0, text))
    }
}