package com.yeyintwin.kbz_movie_db.model

import com.google.gson.annotations.SerializedName

data class MovieResultModel(
    val page: Int,
    val results: List<MovieModel>,
    val total_pages: Int,
    val total_results: Int,
)
