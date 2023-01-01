package com.yeyintwin.kbz_movie_db.model

import com.google.gson.annotations.SerializedName

data class TvSeriesResultModel(
    val page: Int,
    val results: List<TvSeriesModel>,
    val total_pages: Int,
    val total_results: Int,
)
