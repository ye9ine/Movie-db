package com.yeyintwin.kbz_movie_db.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.helper.ServiceHelper
import com.yeyintwin.kbz_movie_db.model.MovieTrailerResultModel
import com.yeyintwin.kbz_movie_db.model.MovieVideosModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieTrailerViewModel() : ViewModel() {

    private val movieTrailerLiveData = MutableLiveData<List<MovieVideosModel>>()
    private val errorMessage = MutableLiveData<String>()

    fun getMovieTrailer(movieId:Int) {

        val response = ServiceHelper.api.getMovieTrailer(movieId)
        response.enqueue(object : Callback<MovieTrailerResultModel> {
            override fun onResponse(call: Call<MovieTrailerResultModel>, response: Response<MovieTrailerResultModel>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val videoList = response.body()?.results
                        var list = mutableListOf<MovieVideosModel>()
                        for (i in videoList!!){
                            if(i.type == MovieDbConstant().videoTrailerKeyWord){
                                list.add(i)
                            }
                            movieTrailerLiveData.value = list
                        }

                    }
                }
            }

            override fun onFailure(call: Call<MovieTrailerResultModel>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun getTvSeriesTrailer(tvId:Int) {

        val response = ServiceHelper.api.getTvSeriesTrailer(tvId)
        response.enqueue(object : Callback<MovieTrailerResultModel> {
            override fun onResponse(call: Call<MovieTrailerResultModel>, response: Response<MovieTrailerResultModel>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        val videoList = response.body()?.results
                        var list = mutableListOf<MovieVideosModel>()
                        for (i in videoList!!){
                            if(i.type == MovieDbConstant().videoTrailerKeyWord){
                                list.add(i)
                            }
                            movieTrailerLiveData.value = list
                        }

                    }
                }
            }

            override fun onFailure(call: Call<MovieTrailerResultModel>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun observeMovieTrailerLiveData() : LiveData<List<MovieVideosModel>> {
        return movieTrailerLiveData
    }

    fun errorMessage() : LiveData<String> {
        return errorMessage
    }
}