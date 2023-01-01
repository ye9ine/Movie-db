package com.yeyintwin.kbz_movie_db.dataSource

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeyintwin.kbz_movie_db.dao.MovieDao
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.helper.ServiceHelper
import com.yeyintwin.kbz_movie_db.model.MovieModel
import kotlinx.coroutines.launch

class MovieDataSource(private val movieDao: MovieDao, private val lifecycleCoroutineScope: LifecycleCoroutineScope) : PagingSource<Int, MovieModel>(){

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

//        val movieDao = DataBase.getDatabase(this).movieDao()

        try {
            val currentLoadingPageKey = params.key ?: 1

            val response = ServiceHelper.api.getMovie(currentLoadingPageKey, )
            val responseData = mutableListOf<MovieModel>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            lifecycleCoroutineScope.launch {
                for(i in responseData){
                    i.type = MovieDbConstant().movie
                    movieDao.addItem(i) }
            }
            val nextKey = if (responseData.isEmpty()) null else currentLoadingPageKey + 1

            if(response.body()?.results?.isEmpty() == true){
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            return LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}