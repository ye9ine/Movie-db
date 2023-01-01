package com.yeyintwin.kbz_movie_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyintwin.kbz_movie_db.model.MovieModel
import com.yeyintwin.kbz_movie_db.model.TvSeriesModel

@Dao
interface TvSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(tvSeriesModel: TvSeriesModel)

    @Query("Select * From tvSeriesTable")
    suspend fun getAll() : List<TvSeriesModel>

    @Query("SELECT * FROM tvSeriesTable WHERE original_name LIKE '%' || :search_query || '%'")
    suspend fun searchTvSeries(search_query: String?): List<TvSeriesModel>
}