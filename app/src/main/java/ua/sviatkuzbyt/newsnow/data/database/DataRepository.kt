package ua.sviatkuzbyt.newsnow.data.database

import ua.sviatkuzbyt.newsnow.data.NewsContainer

var updateDataBaseFromReview = false

class DataRepository(private val request: RequestsNewsData) {
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
}