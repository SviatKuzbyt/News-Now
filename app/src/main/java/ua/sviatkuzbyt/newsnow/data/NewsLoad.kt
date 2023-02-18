package ua.sviatkuzbyt.newsnow.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.json.JSONObject
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

open class NewsLoad(private val request: RequestsNewsData) {
    private var nextPage = ""

    fun loadNews(link: String, firstPage: Boolean): MutableList<NewsContainer>?{
        return try {
            //load data and convert it
            if (nextPage == "&page=null") throw Exception("no more result")
            if (firstPage)
                nextPage = ""
            Log.v("nextPage", nextPage)

            val textUrl = URL(link + nextPage).readText()
            nextPage = "&page=${JSONObject(textUrl).getString("nextPage")}"
            jsonConvert(textUrl)
        }
        catch (e: Exception){
            Log.e("Фігня з загрузкою", e.message.toString())
            null
        }
    }

    fun updateSaved(list: MutableList<NewsContainer>):MutableList<NewsContainer>{
        list.forEach {
            val savedNews = request.isSaved(it.link)
            if (!it.isSaved && savedNews) it.isSaved = true
            else if (it.isSaved && !savedNews) it.isSaved = false
        }
        return list
    }

    private fun jsonConvert(text: String): MutableList<NewsContainer>{
        val list = mutableListOf<NewsContainer>()
        val json = JSONObject(text).getJSONArray("results")

        for (i in 0 until json.length()){
            val jsonObject = json.getJSONObject(i)

            //set date
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date: Date = df.parse(jsonObject.getString("pubDate"))!!
            df.timeZone = TimeZone.getDefault()

            //put element
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
}