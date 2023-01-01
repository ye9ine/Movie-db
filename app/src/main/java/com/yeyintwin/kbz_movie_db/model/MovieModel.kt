package com.yeyintwin.kbz_movie_db.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "movieTable")
data class MovieModel(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val adult: Boolean?,
    @ColumnInfo
    val backdrop_path: String?,
    @ColumnInfo
    val original_language: String?,
    @ColumnInfo
    val original_title: String?,
    @ColumnInfo
    val overview: String?,
    @ColumnInfo
    val popularity: Double?,
    @ColumnInfo
    val poster_path: String?,
    @ColumnInfo
    val release_date: String?,
    @ColumnInfo
    val title: String?,
    @ColumnInfo
    val video: Boolean?,
    @ColumnInfo
    val vote_average: Double?,
    @ColumnInfo
    val vote_count: Int?,
    @ColumnInfo
    val original_name: String?,
    @ColumnInfo
    val first_air_date: String?,
    //local field
    @ColumnInfo
    var type: String?
): Parcelable