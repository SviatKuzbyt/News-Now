package ua.sviatkuzbyt.newsnow.data

import android.graphics.Bitmap

data class NewsContainer(
    val label: String,
    val source: String,
    val time: String,
    var isSaved: Boolean,
    val image: Bitmap?,
    val link: String,
)
