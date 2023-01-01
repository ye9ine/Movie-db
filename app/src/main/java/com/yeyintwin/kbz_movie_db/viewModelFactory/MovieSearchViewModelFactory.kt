package com.yeyintwin.kbz_movie_db.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeyintwin.kbz_movie_db.viewModel.MovieSearchViewModel
import com.yeyintwin.kbz_movie_db.viewModel.MovieViewModel

class MovieSearchViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieSearchViewModel::class.java)){
            return MovieSearchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}