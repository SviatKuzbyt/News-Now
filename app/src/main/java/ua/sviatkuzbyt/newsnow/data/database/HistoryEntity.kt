package ua.sviatkuzbyt.newsnow.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(tableName = "historySearch")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val text: String
)
