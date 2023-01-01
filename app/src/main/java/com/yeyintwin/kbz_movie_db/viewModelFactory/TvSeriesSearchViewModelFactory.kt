package com.yeyintwin.kbz_movie_db.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeyintwin.kbz_movie_db.viewModel.MovieSearchViewModel
import com.yeyintwin.kbz_movie_db.viewModel.MovieViewModel
import com.yeyintwin.kbz_movie_db.viewModel.TvSeriesSearchViewModel

class TvSeriesSearchViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TvSeriesSearchViewModel::class.java)){
            return TvSeriesSearchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}