package ua.sviatkuzbyt.newsnow.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONObject
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ReviewLoad(country: String, private val request: RequestsNewsData) {
    private val urlReview = "https://newsdata.io/api/1/news?" +
            "apikey=pub_11792063ac011beca171231a9b2ae554997ba" +
            "&country=$country"
    private val urlSearch = "https://newsdata.io/api/1/news?" +
            "apikey=pub_11792063ac011beca171231a9b2ae554997ba" +
            "&q="
    fun loadNews(page: Int): MutableList<NewsContainer>?{
        return try {
            val textUrl = URL("$urlReview&page=$page").readText()
            jsonConvert(textUrl)
        } catch (e: Exception){
            null
        }
    }

    fun loadSearch(q: String, page: Int): MutableList<NewsContainer>?{
        return try {
            val textUrl = URL("$urlSearch${q}&page=$page").readText()
            jsonConvert(textUrl)
        } catch (e: Exception){
            null
        }
    }

    private fun jsonConvert(text: String): MutableList<NewsContainer>{
        val list = mutableListOf<NewsContainer>()
        val json = JSONObject(text).getJSONArray("results")

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