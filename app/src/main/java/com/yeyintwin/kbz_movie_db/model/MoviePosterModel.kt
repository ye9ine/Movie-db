package com.yeyintwin.kbz_movie_db.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MoviePosterModel(
    val aspect_ratio: Double,
    val file_path: String,
    val height: Int,
    val iso_639_1: String,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
): Parcelable