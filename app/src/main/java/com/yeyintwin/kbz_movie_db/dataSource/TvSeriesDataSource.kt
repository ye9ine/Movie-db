package com.yeyintwin.kbz_movie_db.dataSource

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeyintwin.kbz_movie_db.dao.TvSeriesDao
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.helper.ServiceHelper
import com.yeyintwin.kbz_movie_db.model.MovieModel
import com.yeyintwin.kbz_movie_db.model.TvSeriesModel
import kotlinx.coroutines.launch

class TvSeriesDataSource(private val tvSeriesDao: TvSeriesDao, private val lifecycleCoroutineScope: LifecycleCoroutineScope) : PagingSource<Int, MovieModel>(){

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        try {
            val currentLoadingPageKey = params.key ?: 1

            val response = ServiceHelper.api.getTv(currentLoadingPageKey)
            val responseData = mutableListOf<MovieModel>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            lifecycleCoroutineScope.launch {
                for(i in responseData){
                    i.type = MovieDbConstant().tvSeries
                    var tvSeriesModel = TvSeriesModel(id = i.id, i.adult, i.backdrop_path, i.original_language, i.original_title, i.overview, i.popularity, i.poster_path, i.release_date,
                    i.title, i.video, i.vote_average, i.vote_count, i.original_name, i. first_air_date, i.type)

                    tvSeriesDao.addItem(tvSeriesModel)
                }
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