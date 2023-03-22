package ua.sviatkuzbyt.newsnow.data.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [SavedNewsEntity::class, HistoryEntity::class], exportSchema = false)
abstract class NewsDataBase : RoomDatabase() {
    abstract fun dao(): NewsDataBaseDao

    companion object {
        @Volatile private var instance: NewsDataBase? = null
        fun getInstance(context: Context): NewsDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NewsDataBase {
            return Room.databaseBuilder(context, NewsDataBase::class.java, "movies-db")
                .build()
        }
    }
}