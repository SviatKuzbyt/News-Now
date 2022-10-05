package ua.sviatkuzbyt.newsnow.data.review

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import org.json.JSONObject
import java.net.URL

class ReviewLoad(country: String, page: Int) {
    private val url = "https://newsdata.io/api/1/news?" +
            "apikey=pub_11792063ac011beca171231a9b2ae554997ba" +
            "&country=$country&page=$page"
    fun loadNews(): MutableList<NewsContainer>{
        val list = mutableListOf<NewsContainer>()
        val textUrl = URL(url).readText()
        val json = JSONObject(textUrl).getJSONArray("results")

        for (i in 0 until json.length()){
            val jsonObject = json.getJSONObject(i)

            list.add(
                NewsContainer(
                jsonObject.getString("title"),
                jsonObject.getString("source_id"),
                jsonObject.getString("pubDate").subSequence(11, 16).toString(),
                false,
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