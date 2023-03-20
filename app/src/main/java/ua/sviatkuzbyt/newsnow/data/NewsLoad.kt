package ua.sviatkuzbyt.newsnow.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.json.JSONObject
import ua.sviatkuzbyt.newsnow.data.database.NewsDataBaseDao
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

open class NewsLoad(private val request: NewsDataBaseDao) {

    lateinit var dataFormat: SimpleDateFormat
    init {
        initDataFormat()
    }

    private fun initDataFormat(){
        dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        dataFormat.timeZone = TimeZone.getTimeZone("UTC")
        dataFormat.timeZone = TimeZone.getDefault()
    }

    fun getListFromUrl(link: String): MutableList<NewsList>{
        val urlResult = URL(link).readText()
        return jsonConvert(urlResult)
    }

    private fun jsonConvert(text: String): MutableList<NewsList>{
        val list = mutableListOf<NewsList>()
        val json = JSONObject(text).getJSONArray("results")

        for (i in 0 until json.length()){
            val jsonObject = json.getJSONObject(i)
            list.add(
                NewsList(
                    jsonObject.getString("title"),
                    jsonObject.getString("source_id"),
                    getDate(jsonObject.getString("pubDate")),
                    request.isSaved(jsonObject.getString("link")),
                    loadImage(jsonObject.optString("image_url")),
                    jsonObject.getString("link")
                )
            )
        }
        return list
    }

    private fun getDate(text: String): String{
        val date: Date = dataFormat.parse(text)!!
        return dataFormat.format(date).subSequence(5, 16).toString()
    }

    private fun loadImage(urlImage: String?): Bitmap? {
        return try {
            URL(urlImage).openStream().use {
                BitmapFactory.decodeStream(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    //old code
    private var nextPage = ""

    fun loadNews(link: String, firstPage: Boolean): MutableList<NewsList>?{
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

    fun updateSaved(list: MutableList<NewsList>):MutableList<NewsList>{
        list.forEach {
            val savedNews = request.isSaved(it.link)
            if (!it.isSaved && savedNews) it.isSaved = true
            else if (it.isSaved && !savedNews) it.isSaved = false
        }
        return list
    }
}