package ua.sviatkuzbyt.newsnow.data.repositories

import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.NewsLoad
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData

class ReviewRepository(request: RequestsNewsData): NewsLoad(request){
    private val key = "pub_11792063ac011beca171231a9b2ae554997ba"
    private val link = "https://newsdata.io/api/1/news?apikey=$key&country="

    fun getRecentlyNews(page: Int, region: String): MutableList<NewsContainer>?{
        return loadNews("$link${region}&page=$page")
    }


}