package ua.sviatkuzbyt.newsnow.data.repositories

import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.ReviewLoad

class ReviewRepository {
    private val connection = ReviewLoad("ua")
    fun getRecentlyNews(page: Int): MutableList<NewsContainer>?{
        return connection.loadNews(page)
    }
}