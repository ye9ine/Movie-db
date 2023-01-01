package com.yeyintwin.kbz_movie_db.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeyintwin.kbz_movie_db.viewModel.MovieViewModel
import com.yeyintwin.kbz_movie_db.viewModel.TvSeriesViewModel

class TvSeriesViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TvSeriesViewModel::class.java)){
            return TvSeriesViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}