package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 2, entities = [SavedNewsEntity::class], exportSchema = false)
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
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }
    }
}