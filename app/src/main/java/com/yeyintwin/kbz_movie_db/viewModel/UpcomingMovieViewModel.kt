package com.yeyintwin.kbz_movie_db.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.helper.ServiceHelper
import com.yeyintwin.kbz_movie_db.model.MovieModel
import com.yeyintwin.kbz_movie_db.model.MovieResultModel
import com.yeyintwin.kbz_movie_db.model.MovieTrailerResultModel
import com.yeyintwin.kbz_movie_db.model.MovieVideosModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingMovieViewModel() : ViewModel() {

    private val upcomingMovieLiveData = MutableLiveData<List<MovieModel>>()
    private val errorMessage = MutableLiveData<String>()
    private val isShowSlider = MutableLiveData<Boolean>()

    fun getUpcomingMovie() {

        val response = ServiceHelper.api.getUpcomingMovie()
        response.enqueue(object : Callback<MovieResultModel> {
            override fun onResponse(call: Call<MovieResultModel>, response: Response<MovieResultModel>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        isShowSlider.value = true
                        val videoList = response.body()?.results
                        upcomingMovieLiveData.value = videoList!!
                    }else{
                        isShowSlider.value = false
                    }
                }else{
                    isShowSlider.value = false
                }
            }

            override fun onFailure(call: Call<MovieResultModel>, t: Throwable) {
                errorMessage.value = t.message
                isShowSlider.value = false
            }
        })
    }

    fun observeUpcomingMovieLiveData() : LiveData<List<MovieModel>> {
        return upcomingMovieLiveData
    }

    fun isShowSlider() : LiveData<Boolean> {
        return isShowSlider
    }

    fun errorMessage() : LiveData<String> {
        return errorMessage
    }
}