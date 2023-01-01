package com.yeyintwin.kbz_movie_db.model

import com.google.gson.annotations.SerializedName
import com.yeyintwin.kbz_movie_db.viewModel.MovieViewModel

data class MovieTrailerResultModel(
    val id: Int,
    val results: List<MovieVideosModel>,
)
