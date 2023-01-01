package com.yeyintwin.kbz_movie_db.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeyintwin.kbz_movie_db.viewModel.MovieViewModel

class MovieViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieViewModel::class.java)){
            return MovieViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}