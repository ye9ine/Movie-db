package com.yeyintwin.kbz_movie_db.viewModelFactory

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeyintwin.kbz_movie_db.dao.MovieDao
import com.yeyintwin.kbz_movie_db.viewModel.MovieViewModel

class MovieViewModelFactory(private val movieDao: MovieDao, private val lifecycleCoroutineScope: LifecycleCoroutineScope) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieViewModel::class.java)){
            return MovieViewModel(movieDao, lifecycleCoroutineScope) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}