package ua.sviatkuzbyt.newsnow.data.loadlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsTableRepository
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

open class NewsLoad(private val savedNewsTableRepository: SavedNewsTableRepository) {

    private val imageOptions = BitmapFactory.Options()
    private val utcDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val localDateFormat = SimpleDateFormat("MMM dd, hh:mm", Locale.getDefault())

    init {
        imageOptions.inSampleSize = 2
        utcDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    suspend fun getListFromUrl(link: String): NewsContainer {
        val urlResult = URL(link).readText()
        val list = jsonConvert(urlResult)
        val nextPage = getNextPage(urlResult)
        return NewsContainer(list, nextPage)
    }

    private suspend fun jsonConvert(text: String): MutableList<NewsList> {
        val list = mutableListOf<NewsList>()
        withContext(Dispatchers.IO){
            val json = JSONObject(text).getJSONArray("results")
            for (i in 0 until json.length()) {
                if(isActive){
                    val jsonObject = json.getJSONObject(i)
                    list.add(
                        NewsList(
                            jsonObject.getString("title"),
                            jsonObject.getString("source_id"),
                            formatDate(jsonObject.getString("pubDate")),
                            savedNewsTableRepository.isSaved(jsonObject.getString("link")),
                            loadImage(jsonObject.optString("image_url")),
                            jsonObject.getString("link")
                        )
                    )
                }
            }
        }
        return list
    }

    private fun formatDate(dateString: String) = try {
        val date = utcDateFormat.parse(dateString)
        localDateFormat.format(date as Date)
    } catch (e: ParseException) { "" }


    private fun loadImage(urlImage: String?): Bitmap? {
        return try {
            URL(urlImage).openStream().use {
                BitmapFactory.decodeStream(it, null, imageOptions)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getNextPage(text: String): String {
        val json = JSONObject(text)
        return json.getString("nextPage")
    }

    data class NewsContainer(
        val newsList: MutableList<NewsList>,
        val nextPage: String
    )
}