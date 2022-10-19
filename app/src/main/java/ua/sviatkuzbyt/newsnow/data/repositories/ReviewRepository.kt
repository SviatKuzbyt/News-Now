package ua.sviatkuzbyt.newsnow.data.repositories

import android.util.Log
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.ReviewLoad
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsEntity

class ReviewRepository(private val request: RequestsNewsData) {
    private val connection = ReviewLoad("ua", request)
    fun getRecentlyNews(page: Int): MutableList<NewsContainer>?{
        Log.v("reps","Воно довге, сука")
        val list = connection.loadNews(page)
        Log.v("reps","Воно не довге, сука")
//        list?.forEach {
//            if(request.isSaved(it.link)) it.isSaved = true
//        }
        return list
    }
}