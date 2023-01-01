package com.yeyintwin.kbz_movie_db.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeyintwin.kbz_movie_db.helper.ServiceHelper
import com.yeyintwin.kbz_movie_db.model.MoviePosterModel
import com.yeyintwin.kbz_movie_db.model.MoviePosterResultModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviePosterViewModel() : ViewModel() {

    private val moviePosterLiveData = MutableLiveData<ArrayList<MoviePosterModel>>()
    private val errorMessage = MutableLiveData<String>()

    fun getMoviePoster(movieId:Int) {

        val response = ServiceHelper.api.getMoviePoster(movieId)
        response.enqueue(object : Callback<MoviePosterResultModel> {
            override fun onResponse(call: Call<MoviePosterResultModel>, response: Response<MoviePosterResultModel>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        moviePosterLiveData.value = response.body()?.posters
                    }
                }
            }

            override fun onFailure(call: Call<MoviePosterResultModel>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun getTvSeriesPoster(tvId:Int) {

        val response = ServiceHelper.api.getTvSeriesPoster(tvId)
        response.enqueue(object : Callback<MoviePosterResultModel> {
            override fun onResponse(call: Call<MoviePosterResultModel>, response: Response<MoviePosterResultModel>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        moviePosterLiveData.value = response.body()?.posters
                    }
                }
            }

            override fun onFailure(call: Call<MoviePosterResultModel>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun observeMoviePosterLiveData() : MutableLiveData<ArrayList<MoviePosterModel>> {
        return moviePosterLiveData
    }

    fun errorMessage() : LiveData<String> {
        return errorMessage
    }
}