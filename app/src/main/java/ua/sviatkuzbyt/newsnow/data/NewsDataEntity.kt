package ua.sviatkuzbyt.newsnow.data

import android.graphics.Bitmap

data class NewsDataEntity(
    val label: String,
    val source: String,
    val time: String,
    val isSaved: Boolean,
    val image: Bitmap?,
    val link: String
)
