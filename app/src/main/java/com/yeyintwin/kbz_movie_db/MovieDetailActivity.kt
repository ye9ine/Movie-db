package com.yeyintwin.kbz_movie_db

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import com.yeyintwin.kbz_movie_db.adapter.MoviePosterAdapter
import com.yeyintwin.kbz_movie_db.databinding.ActivityMovieDetailBinding
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.model.MovieModel
import com.yeyintwin.kbz_movie_db.viewModel.MoviePosterViewModel
import com.yeyintwin.kbz_movie_db.viewModel.MovieTrailerViewModel


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailBinding: ActivityMovieDetailBinding
    private lateinit var moviePosterViewModel: MoviePosterViewModel
    private lateinit var moviePosterAdapter: MoviePosterAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var movieTrailerViewModel: MovieTrailerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init view binding
        movieDetailBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(movieDetailBinding.root)
        init()
    }

    private fun init(){

        //get data from pass intent
        val movieModel = getParcelableExtra(intent, "obj", MovieModel::class.java)

        //init adapter and layout manager
        linearLayoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL, false)
        moviePosterAdapter = MoviePosterAdapter(ArrayList())

        movieDetailBinding.apply {

            //binding data to view
            val releaseYear = if (movieModel?.type == MovieDbConstant().movie) movieModel.release_date?.split('-')?.get(0) else movieModel?.first_air_date?.split('-')?.get(0)
            if (movieModel != null) {
                tvTitle.text = if (movieModel?.type == MovieDbConstant().movie) "${movieModel.title} (${releaseYear})" else "${movieModel.original_name} (${releaseYear})"
            }
            rtBar.rating = movieModel?.vote_average?.div(2).toString().toFloat()
            tvRating.text = movieModel?.vote_average.toString()
            tvOverview.text = movieModel?.overview

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = moviePosterAdapter
            }

            if(movieModel?.backdrop_path != null){
                Glide.with(applicationContext)
                    .load(MovieDbConstant().baseBackdropPath + movieModel.backdrop_path)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .into(imgBackDrop)
            }

            imgBackButton.setOnClickListener{
                finish()
            }

            lifecycle.addObserver(youtubePlayerView)

        }


        //init view model
        movieTrailerViewModel = ViewModelProvider(this@MovieDetailActivity)[MovieTrailerViewModel::class.java]


        //fetch movie or tv trailer video
        if (movieModel != null) {
            if(movieModel.type == MovieDbConstant().movie){
                movieTrailerViewModel.getMovieTrailer(movieModel.id)
            }else{
                movieTrailerViewModel.getTvSeriesTrailer(movieModel.id)
            }
        }


        //init and cue youtube player
        movieTrailerViewModel.observeMovieTrailerLiveData().observe(this@MovieDetailActivity, Observer {
            val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val defaultPlayerUiController = DefaultPlayerUiController(movieDetailBinding.youtubePlayerView, youTubePlayer)
                    movieDetailBinding.youtubePlayerView.setCustomPlayerUi(defaultPlayerUiController.rootView)
                    youTubePlayer.cueVideo(it.first().key, 0F)
                }
            }

            val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
            movieDetailBinding.youtubePlayerView.initialize(listener, options)

        })


        //init poster view model
        moviePosterViewModel = ViewModelProvider(this@MovieDetailActivity)[MoviePosterViewModel::class.java]


        //fetch movie or tv poster
        if (movieModel != null) {
            if (movieModel.type == MovieDbConstant().movie) {
                moviePosterViewModel.getMoviePoster(movieModel.id)
            }else{
                moviePosterViewModel.getTvSeriesPoster(movieModel.id)
            }

        }

        //bind adapter
        moviePosterViewModel.observeMoviePosterLiveData().observe(this@MovieDetailActivity, Observer {
            moviePosterAdapter.addAll(it)
        })


    }


    override fun onDestroy() {
        movieDetailBinding.youtubePlayerView.release()
        super.onDestroy()
    }

    private fun <T : Parcelable> getParcelableExtra(intent: Intent, key: String, m_class: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(key, m_class)!!
        else
            intent.getParcelableExtra(key) as? T
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}