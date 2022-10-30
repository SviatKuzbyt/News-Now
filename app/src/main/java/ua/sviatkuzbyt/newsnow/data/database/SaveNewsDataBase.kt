package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [SavedNewsEntity::class, HistoryEntity::class], exportSchema = false)
abstract class SaveNewsDataBase : RoomDatabase() {
    abstract fun request(): RequestsNewsData

    companion object {
        @Volatile
        private var INSTANCE: SaveNewsDataBase? = null

        fun getDatabase(context: Context): SaveNewsDataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SaveNewsDataBase::class.java,
                    "savednews"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}