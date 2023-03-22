package ua.sviatkuzbyt.newsnow.data.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historySearch")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val text: String
)
