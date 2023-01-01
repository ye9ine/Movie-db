package com.yeyintwin.kbz_movie_db.viewModel

import androidx.lifecycle.*
import androidx.paging.*
import com.yeyintwin.kbz_movie_db.dao.MovieDao
import com.yeyintwin.kbz_movie_db.dataSource.MovieDataSource

class MovieViewModel(private val movieDao: MovieDao, private val lifecycleCoroutineScope: LifecycleCoroutineScope) : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 10)) {
        MovieDataSource(movieDao, lifecycleCoroutineScope)
    }.flow.cachedIn(viewModelScope)
}