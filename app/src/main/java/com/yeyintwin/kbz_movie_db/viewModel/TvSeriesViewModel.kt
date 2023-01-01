package com.yeyintwin.kbz_movie_db.viewModel

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.yeyintwin.kbz_movie_db.dao.TvSeriesDao
import com.yeyintwin.kbz_movie_db.dataSource.TvSeriesDataSource

class TvSeriesViewModel(private val tvSeriesDao: TvSeriesDao, private val lifecycleCoroutineScope: LifecycleCoroutineScope) : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 10)) {
        TvSeriesDataSource(tvSeriesDao, lifecycleCoroutineScope = lifecycleCoroutineScope)
    }.flow.cachedIn(viewModelScope)
}