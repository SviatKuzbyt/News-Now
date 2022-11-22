package ua.sviatkuzbyt.newsnow.data.repositories

import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.NewsLoad
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData

class SearchRepository(request: RequestsNewsData): NewsLoad(request) {
    private val key = "pub_11792063ac011beca171231a9b2ae554997ba"
    private val link = "https://newsdata.io/api/1/news?apikey=$key&q="

    fun search(q: String, language: String, page: Int): MutableList<NewsContainer>?{
        val link = "$link$q&page=$page${
            if (language.isNotEmpty()) "&language=$language" 
            else ""
        }"

        return loadNews(link)
    }
}