package com.yeyintwin.kbz_movie_db.model

import com.google.gson.annotations.SerializedName

data class MoviePosterResultModel(
    val id: Int,
    val backdrops: List<MoviePosterModel>,
    val logos: List<MoviePosterModel>,
    val posters: ArrayList<MoviePosterModel>,
)
