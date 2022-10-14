package ua.sviatkuzbyt.newsnow.data.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savednews")
data class SavedNewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val source: String,
    val time: String,
    val image: String,
    val link: String
)
