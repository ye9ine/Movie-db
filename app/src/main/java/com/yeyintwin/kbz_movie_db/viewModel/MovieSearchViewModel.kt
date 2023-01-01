package com.yeyintwin.kbz_movie_db.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.yeyintwin.kbz_movie_db.dataSource.MovieDataSource
import com.yeyintwin.kbz_movie_db.dataSource.MovieSearchDataSource
import com.yeyintwin.kbz_movie_db.helper.ServiceHelper
import com.yeyintwin.kbz_movie_db.model.MovieModel
import com.yeyintwin.kbz_movie_db.model.MovieResultModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSearchViewModel : ViewModel() {

    private val _searchKey = MutableLiveData<String>()

    val listData = Pager(PagingConfig(pageSize = 10)) {
        MovieSearchDataSource(_searchKey.value!!)
    }.flow.cachedIn(viewModelScope)

    fun setCurrentQuery(query: String){
        _searchKey.value = query
    }
}