package com.yeyintwin.kbz_movie_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyintwin.kbz_movie_db.model.MovieModel

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(movieModel: MovieModel)

    @Query("Select * From movieTable")
    suspend fun getAll() : List<MovieModel>

    @Query("SELECT * FROM movieTable WHERE original_title LIKE '%' || :search_query || '%'")
    suspend fun searchMovie(search_query: String?): List<MovieModel>
}