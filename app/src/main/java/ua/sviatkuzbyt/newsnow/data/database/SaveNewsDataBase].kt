package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class SaveNewsDataBase {
}
@Database(version = 1, entities = [SavedNewsEntity::class], exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun request(): RequestsNewsData

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "savednews"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}