package ua.sviatkuzbyt.newsnow.data.other

import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository

fun getNewsListFromSavedDataBase(db: DataBaseRepository): MutableList<NewsList>{
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