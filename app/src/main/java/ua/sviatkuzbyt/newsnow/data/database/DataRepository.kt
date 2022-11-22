package ua.sviatkuzbyt.newsnow.data.database

import ua.sviatkuzbyt.newsnow.data.NewsContainer

class DataRepository(private val request: RequestsNewsData) {
    //functions for saved table
    fun addSavedNews(item: NewsContainer){
        request.addSaveNews(
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
        request.deleteSaveNews(item)
    }
    fun getData(): MutableList<SavedNewsEntity> = request.getSavedNews()

    //function for history table
    fun getHistory() = request.getHistory()
    fun deleteHistory(history: String){ request.deleteHistory(history) }

    fun addHistory(text: String) {
        request.addHistory(HistoryEntity(
            0,
            text
        ))
    }
}