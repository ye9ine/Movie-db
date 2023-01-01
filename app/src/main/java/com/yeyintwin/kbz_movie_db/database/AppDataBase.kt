package com.yeyintwin.kbz_movie_db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeyintwin.kbz_movie_db.dao.MovieDao
import com.yeyintwin.kbz_movie_db.dao.TvSeriesDao
import com.yeyintwin.kbz_movie_db.model.MovieModel
import com.yeyintwin.kbz_movie_db.model.TvSeriesModel

@Database(
    entities = [MovieModel::class, TvSeriesModel::class],
    version = 1
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun movieDao() : MovieDao
    abstract fun tvSeriesDao() : TvSeriesDao

    companion object{
        @Volatile
        private var instance: AppDataBase? = null
        fun getDatabase(context: Context): AppDataBase{
            if(instance == null){
                synchronized(this){
                    instance = buildDatabase(context)
                }
            }

            return instance!!
        }

        /*private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // The following query will add a new column called lastUpdate to the notes database
                database.execSQL("ALTER TABLE itemTable ADD COLUMN lastUpdate INTEGER NOT NULL DEFAULT 0")
            }
        }*/

        private fun buildDatabase(context: Context):AppDataBase{
            return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "database"
            ).build()
        }
    }
}