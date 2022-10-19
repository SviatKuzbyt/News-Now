package ua.sviatkuzbyt.newsnow.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import ua.sviatkuzbyt.newsnow.MainActivity
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ReviewLoad(country: String, private val request: RequestsNewsData) {
    private val url = "https://newsdata.io/api/1/news?" +
            "apikey=pub_1228749eee196a77f494bc549964a1cd5318c" +
            "&country=$country"
    fun loadNews(page: Int): MutableList<NewsContainer>?{
        val list = mutableListOf<NewsContainer>()
        try {
            val textUrl = URL("$url&page=$page").readText()
            val json = JSONObject(textUrl).getJSONArray("results")

            for (i in 0 until json.length()){
                val jsonObject = json.getJSONObject(i)

                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                df.timeZone = TimeZone.getTimeZone("UTC")
                val date: Date = df.parse(jsonObject.getString("pubDate"))!!
                df.timeZone = TimeZone.getDefault()

                list.add(
                    NewsContainer(
                        jsonObject.getString("title"),
                        jsonObject.getString("source_id"),
                        df.format(date).subSequence(5, 16).toString(),
                        request.isSaved(jsonObject.getString("link")),
                        loadImage(jsonObject.getString("image_url")),
                        jsonObject.getString("link")
                    )
                )
            }
            return list
        } catch (e: Exception){
            return null
        }

    }

    private fun loadImage(urlImage: String): Bitmap? {
        return try {
            if (urlImage != "null") {
                val stream = URL(urlImage).openStream()
                BitmapFactory.decodeStream(stream)
            } else null
        } catch (e: Exception) {
            null
        }

    }

//    fun loadNews(page: Int): MutableList<NewsContainer>{
//    return runBlocking {
//        delay(2000)
//        return@runBlocking mutableListOf(
//            NewsContainer(page.toString(), "12423", "11:45", false, null, "https://google.com"),
//            NewsContainer(page.toString(), "12423", "11:45", false, null, "https://google.com"),
//            NewsContainer(page.toString(), "12423", "11:45", false, null, "https://google.com"),
//            NewsContainer(page.toString(), "12423", "11:45", false, null, "https://google.com")
//        )
//    }
//    }

}