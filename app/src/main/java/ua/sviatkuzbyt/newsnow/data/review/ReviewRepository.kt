package ua.sviatkuzbyt.newsnow.data.review

import ua.sviatkuzbyt.newsnow.data.NewsContainer

class ReviewRepository {
    private val connection = ReviewLoad("us", 0)
    fun getRecentlyNews(): List<NewsContainer>{
        return connection.loadNews()
    }
}