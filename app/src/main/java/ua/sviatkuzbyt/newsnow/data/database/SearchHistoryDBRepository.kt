package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import ua.sviatkuzbyt.newsnow.data.database.room.HistoryEntity
import ua.sviatkuzbyt.newsnow.data.database.room.NewsDataBase

class SearchHistoryDBRepository(context: Context) {
    private val dao = NewsDataBase.getInstance(context).dao()
    fun getHistory() = dao.getHistory()

    fun deleteHistory(history: String){
        dao.deleteHistory(history)
    }

    fun addHistory(text: String) {
        dao.addHistory(HistoryEntity(0, text))
    }
}