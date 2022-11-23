package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import android.widget.Toast
import ua.sviatkuzbyt.newsnow.data.NewsContainer

class DataRepository(private val request: RequestsNewsData, private val context: Context) {
    //functions for saved table
    fun addSavedNews(item: NewsContainer){
        try {
            request.addSaveNews(
                SavedNewsEntity(
                    0,
                    item.label,
                    item.source,
                    item.time,
                    item.link
                )
            )
        } catch (e: Exception){ makeToast() }
    }

    fun removeSavedNews(item: String){
        try {
            request.deleteSaveNews(item)
        } catch (e: Exception){ makeToast() }

    }
    fun getData(): MutableList<SavedNewsEntity> =
        try {
            request.getSavedNews()
        } catch (e: Exception){
            makeToast()
            mutableListOf()
        }

//function for history table
    fun getHistory() = try {
        request.getHistory()
    }   catch (e: Exception){
        makeToast()
        mutableListOf()
    }

    fun deleteHistory(history: String){
        try {
            request.deleteHistory(history)
        } catch (e: Exception){ makeToast() }

    }

    fun addHistory(text: String) {
        try {
            request.addHistory(HistoryEntity(0, text))
        } catch (e: Exception){ makeToast() }
    }

    private fun makeToast(){
        Toast.makeText(context, "DB error", Toast.LENGTH_SHORT).show()
    }
}