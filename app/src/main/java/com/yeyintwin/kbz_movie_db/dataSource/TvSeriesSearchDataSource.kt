package com.yeyintwin.kbz_movie_db.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeyintwin.kbz_movie_db.helper.ServiceHelper
import com.yeyintwin.kbz_movie_db.model.MovieModel

class TvSeriesSearchDataSource(private val searchKey: String) : PagingSource<Int, MovieModel>(){

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = ServiceHelper.api.searchTvSeries(searchKey, currentLoadingPageKey)
            val responseData = mutableListOf<MovieModel>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

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