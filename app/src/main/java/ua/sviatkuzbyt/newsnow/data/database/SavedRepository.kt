package ua.sviatkuzbyt.newsnow.data.database

import ua.sviatkuzbyt.newsnow.data.other.NewsList

class SavedRepository(private val db: DataBaseRepository) {

    fun getList(): MutableList<NewsList>{
        val savedNews = db.getSavedNews()
        val listResult = mutableListOf<NewsList>()

        savedNews.forEach {
            listResult.add(
                NewsList(
                it.label,
                    it.source,
                    it.time,
                    true,
                    null,
                    it.link
            )
            )
        }

        return listResult
    }
}