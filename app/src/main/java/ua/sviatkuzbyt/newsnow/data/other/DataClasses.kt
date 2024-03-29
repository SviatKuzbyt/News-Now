package ua.sviatkuzbyt.newsnow.data.other

import android.graphics.Bitmap

data class NewsList(
    val label: String,
    val source: String,
    val time: String,
    var isSaved: Boolean,
    val image: Bitmap?,
    val link: String,
)

data class SettingValues(
    val value: String,
    val code: String
)