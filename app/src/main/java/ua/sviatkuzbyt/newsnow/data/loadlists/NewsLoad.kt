package ua.sviatkuzbyt.newsnow.data.loadlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONObject
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsDBRepository
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

open class NewsLoad(private val savedNewsDBRepository: SavedNewsDBRepository) {

    private lateinit var dataFormat: SimpleDateFormat
    init {
        initDataFormat()
    }

    private fun initDataFormat(){
        dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        dataFormat.timeZone = TimeZone.getTimeZone("UTC")
        dataFormat.timeZone = TimeZone.getDefault()
    }

    fun getListFromUrl(link: String): NewsContainer {
        val urlResult = URL(link).readText()
        val list = jsonConvert(urlResult)
        val nextPage = getNextPage(urlResult)
        return NewsContainer(list, nextPage)
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
                    savedNewsDBRepository.isSaved(jsonObject.getString("link")),
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

    private fun getNextPage(text: String): String{
        val json = JSONObject(text)
        return json.getString("nextPage")
    }

    data class NewsContainer(
        val newsList: MutableList<NewsList>,
        val nextPage: String
    )
}