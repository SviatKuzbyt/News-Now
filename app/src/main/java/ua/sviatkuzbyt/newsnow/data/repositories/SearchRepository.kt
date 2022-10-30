package ua.sviatkuzbyt.newsnow.data.repositories

import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.ReviewLoad
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData

class SearchRepository(request: RequestsNewsData) {
    private val connection = ReviewLoad("ua", request)

    fun search(q: String, page: Int): MutableList<NewsContainer>?{
        return connection.loadSearch(q, page)
    }
}