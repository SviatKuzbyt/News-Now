package ua.sviatkuzbyt.newsnow.data.repositories

import ua.sviatkuzbyt.newsnow.data.NewsLoad
import ua.sviatkuzbyt.newsnow.data.database.NewsDataBaseDao

class ReviewRepository(request: NewsDataBaseDao): NewsLoad(request){
    private val key = "pub_11792063ac011beca171231a9b2ae554997ba"
    private val link = "https://newsdata.io/api/1/news?apikey=$key&country="

    fun getRecentlyNews(firstPage: Boolean, region: String) =
        loadNews("$link${region}", firstPage)
}