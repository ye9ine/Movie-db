package com.yeyintwin.kbz_movie_db.viewModelFactory

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeyintwin.kbz_movie_db.dao.TvSeriesDao
import com.yeyintwin.kbz_movie_db.viewModel.TvSeriesViewModel

class TvSeriesViewModelFactory(private val tvSeriesDao: TvSeriesDao, private val lifecycleCoroutineScope: LifecycleCoroutineScope) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TvSeriesViewModel::class.java)){
            return TvSeriesViewModel(tvSeriesDao, lifecycleCoroutineScope) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}