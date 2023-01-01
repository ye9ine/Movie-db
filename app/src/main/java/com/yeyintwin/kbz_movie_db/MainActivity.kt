package com.yeyintwin.kbz_movie_db

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.yeyintwin.kbz_movie_db.adapter.MovieListAdapter
import com.yeyintwin.kbz_movie_db.dao.MovieDao
import com.yeyintwin.kbz_movie_db.dao.TvSeriesDao
import com.yeyintwin.kbz_movie_db.database.AppDataBase
import com.yeyintwin.kbz_movie_db.databinding.ActivityMainBinding
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.model.MovieModel
import com.yeyintwin.kbz_movie_db.model.TvSeriesModel
import com.yeyintwin.kbz_movie_db.viewModel.*
import com.yeyintwin.kbz_movie_db.viewModelFactory.MovieSearchViewModelFactory
import com.yeyintwin.kbz_movie_db.viewModelFactory.MovieViewModelFactory
import com.yeyintwin.kbz_movie_db.viewModelFactory.TvSeriesSearchViewModelFactory
import com.yeyintwin.kbz_movie_db.viewModelFactory.TvSeriesViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), OnRefreshListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieSearchViewModel: MovieSearchViewModel
    private lateinit var tvSeriesSearchViewModel: TvSeriesSearchViewModel
    private lateinit var upcomingMovieViewModel: UpcomingMovieViewModel
    private lateinit var tvSeriesViewModel: TvSeriesViewModel
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var type = MovieDbConstant().movie
    private lateinit var dataBase: AppDataBase
    private lateinit var movieDao: MovieDao
    private lateinit var tvSeriesDao: TvSeriesDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init view binding
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        init()
    }



    private fun init(){
        //init toolbar
        setSupportActionBar(mainBinding.toolbar.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = "The Movie Db"
        }

        //init room db
        dataBase = AppDataBase.getDatabase(this)
        movieDao = dataBase.movieDao()
        tvSeriesDao = dataBase.tvSeriesDao()

        //init adapter and layout manager
        movieListAdapter = MovieListAdapter(type)
        linearLayoutManager = LinearLayoutManager(applicationContext)

        mainBinding.apply {

            toolbar.toolbar.setNavigationIcon(R.mipmap.toolbar_logo)
            tvInTheaters.background = ContextCompat.getDrawable(applicationContext, R.drawable.pill_shape_background)
            swipeRefresh.setOnRefreshListener(this@MainActivity)

            //init recycler view
            recyclerView.apply {
                layoutManager = linearLayoutManager
                setHasFixedSize(true)
                adapter = movieListAdapter
            }

            imgRetry.setOnClickListener {
                movieListAdapter.refresh()
            }

            //toggle movie
            tvInTheaters.setOnClickListener {
                type = MovieDbConstant().movie
                tvInTheaters.background = ContextCompat.getDrawable(applicationContext, R.drawable.pill_shape_background)
                tvOnTv.background = null
                movieListAdapter = MovieListAdapter(type)
                mainBinding.recyclerView.adapter = movieListAdapter

                lifecycleScope.launch{
                    movieListAdapter.loadStateFlow.collectLatest { state->
                        mainBinding.progressBar.isVisible = state.refresh is LoadState.Loading
                        if(state.refresh is LoadState.Error){
                            var list = movieDao.getAll()
                            movieListAdapter.submitData(PagingData.from(list))
                        }
                    }
                }

                lifecycleScope.launch{
                    viewModel.listData.collect{ it ->
                        movieListAdapter.submitData(it)
                    }
                }
                movieListAdapter.refresh()
            }

            //toggle tv
            tvOnTv.setOnClickListener {
                type = MovieDbConstant().tvSeries
                tvOnTv.background = ContextCompat.getDrawable(applicationContext, R.drawable.pill_shape_background)
                tvInTheaters.background = null
                movieListAdapter = MovieListAdapter(type)
                mainBinding.recyclerView.adapter = movieListAdapter

                lifecycleScope.launch{
                    movieListAdapter.loadStateFlow.collectLatest { state->
                        mainBinding.progressBar.isVisible = state.refresh is LoadState.Loading
                        if(state.refresh is LoadState.Error){
                            var list = tvSeriesDao.getAll()
                            var movieList = arrayListOf<MovieModel>()
                            for(i in list){
                                var model = MovieModel(id = i.id, i.adult, i.backdrop_path, i.original_language, i.original_title, i.overview, i.popularity, i.poster_path, i.release_date,
                                    i.title, i.video, i.vote_average, i.vote_count, i.original_name, i. first_air_date, i.type)
                                movieList.add(model)
                            }
                            movieListAdapter.submitData(PagingData.from(movieList))
                        }

                    }
                }

                lifecycleScope.launch{
                    tvSeriesViewModel.listData.collect{ it ->
                        movieListAdapter.submitData(it)
                    }
                }

                movieListAdapter.refresh()
            }
        }


        //init view model
        viewModel = ViewModelProvider(this, MovieViewModelFactory(movieDao, lifecycleScope))[MovieViewModel::class.java]
        movieSearchViewModel = ViewModelProvider(this, MovieSearchViewModelFactory())[MovieSearchViewModel::class.java]
        tvSeriesSearchViewModel = ViewModelProvider(this, TvSeriesSearchViewModelFactory())[TvSeriesSearchViewModel::class.java]
        upcomingMovieViewModel = ViewModelProvider(this)[UpcomingMovieViewModel::class.java]
        tvSeriesViewModel = ViewModelProvider(this, TvSeriesViewModelFactory(tvSeriesDao, lifecycleScope))[TvSeriesViewModel::class.java]

        //fetch upcoming movie data
        upcomingMovieViewModel.getUpcomingMovie()

        upcomingMovieViewModel.isShowSlider().observe(this, Observer {
            mainBinding.sliderLayout.visibility = if(it) View.VISIBLE else View.GONE
            mainBinding.tvUpcoming.visibility = if(it) View.VISIBLE else View.GONE
        })

        //show and bind upcoming movie data to slider layout
        upcomingMovieViewModel.observeUpcomingMovieLiveData().observe(this, Observer {
            mainBinding.sliderLayout.removeAllSliders()
            Log.d("upcoming", it.size.toString())
            for(i in it){
                var textSliderView = TextSliderView(this)
                textSliderView.apply {
                    description(i.title)
                    image(MovieDbConstant().baseBackdropPath + i.backdrop_path)
                    scaleType = BaseSliderView.ScaleType.CenterCrop
                    setOnSliderClickListener(this@MainActivity)
                    bundle(Bundle())
                    bundle.putParcelable("obj", i)
                }

                mainBinding.sliderLayout.apply {
                    addSlider(textSliderView)
                    setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
                    setCustomAnimation(DescriptionAnimation())
                    setDuration(5000)
                    addOnPageChangeListener(this@MainActivity)
                }
            }
        })


        //init paging loading state
        lifecycleScope.launch{
            movieListAdapter.loadStateFlow.collectLatest { state->
                mainBinding.progressBar.isVisible = state.refresh is LoadState.Loading
                /*mainBinding.imgNetworkError.isVisible = state.refresh is LoadState.Error
                mainBinding.imgRetry.isVisible = state.refresh is LoadState.Error*/
                if(state.refresh is LoadState.Error){
                    var list = movieDao.getAll()
                    movieListAdapter.submitData(PagingData.from(list))
                }

            }
        }

        //fetch movie data
        lifecycleScope.launch{
            viewModel.listData.collect{ it ->
                movieListAdapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu?.findItem(R.id.appSearchBar)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search movie"
        searchView.setOnCloseListener {

            lifecycleScope.launch{
                movieListAdapter.loadStateFlow.collectLatest { state->
                    mainBinding.progressBar.isVisible = state.refresh is LoadState.Loading
                    if(state.refresh is LoadState.Error){
                        if(type == MovieDbConstant().movie){
                            var list = movieDao.getAll()
                            movieListAdapter.submitData(PagingData.from(list))
                        }else{
                            var list = tvSeriesDao.getAll()
                            var movieList = arrayListOf<MovieModel>()
                            for(i in list){
                                var model = MovieModel(id = i.id, i.adult, i.backdrop_path, i.original_language, i.original_title, i.overview, i.popularity, i.poster_path, i.release_date,
                                    i.title, i.video, i.vote_average, i.vote_count, i.original_name, i. first_air_date, i.type)
                                movieList.add(model)
                            }
                            movieListAdapter.submitData(PagingData.from(movieList))
                        }

                    }else{
                        if(type == MovieDbConstant().movie){
                            viewModel.listData.collect{ it ->
                                movieListAdapter.submitData(it)
                            }
                        }else{
                            tvSeriesViewModel.listData.collect{ it ->
                                movieListAdapter.submitData(it)
                            }
                        }
                    }
                }
            }

            movieListAdapter.refresh()
            false
        }

        //search view function
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                //when press search button dissmiss keyboard
                this@MainActivity.dismissKeyboard()

                lifecycleScope.launch{
                    movieListAdapter.loadStateFlow.collectLatest { state->
                        mainBinding.progressBar.isVisible = state.refresh is LoadState.Loading
                        if(state.refresh is LoadState.Error){
                            if(type == MovieDbConstant().movie){
                                var list = movieDao.searchMovie(query)
                                movieListAdapter.submitData(PagingData.from(list))
                            }else{
                                var list = tvSeriesDao.searchTvSeries(query)
                                var movieList = arrayListOf<MovieModel>()
                                for(i in list){
                                    var model = MovieModel(id = i.id, i.adult, i.backdrop_path, i.original_language, i.original_title, i.overview, i.popularity, i.poster_path, i.release_date,
                                        i.title, i.video, i.vote_average, i.vote_count, i.original_name, i. first_air_date, i.type)
                                    movieList.add(model)
                                }
                                movieListAdapter.submitData(PagingData.from(movieList))
                            }

                        }else{
                            if(type == MovieDbConstant().movie){
                                movieSearchViewModel.setCurrentQuery(query!!)
                                movieSearchViewModel.listData.collect{ it ->
                                    movieListAdapter.submitData(it)
                                }
                            }else{
                                tvSeriesSearchViewModel.setCurrentQuery(query!!)
                                tvSeriesSearchViewModel.listData.collect{ it ->
                                    movieListAdapter.submitData(it)
                                }
                            }
                        }
                    }
                }

                movieListAdapter.refresh()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    //dismiss keyboard function
    fun Activity.dismissKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE ) as InputMethodManager
        if( inputMethodManager.isAcceptingText )
            if(this.currentFocus != null){
                inputMethodManager.hideSoftInputFromWindow( this.currentFocus!!.windowToken, 0)
            }

    }

    override fun onRefresh() {
        mainBinding.swipeRefresh.isRefreshing = false
        lifecycleScope.launch{
            movieListAdapter.loadStateFlow.collectLatest { state->
                mainBinding.progressBar.isVisible = state.refresh is LoadState.Loading
                if(state.refresh is LoadState.Error){
                    if(type == MovieDbConstant().movie){
                        var list = movieDao.getAll()
                        movieListAdapter.submitData(PagingData.from(list))
                    }else{
                        var list = tvSeriesDao.getAll()
                        var movieList = arrayListOf<MovieModel>()
                        for(i in list){
                            var model = MovieModel(id = i.id, i.adult, i.backdrop_path, i.original_language, i.original_title, i.overview, i.popularity, i.poster_path, i.release_date,
                                i.title, i.video, i.vote_average, i.vote_count, i.original_name, i. first_air_date, i.type)
                            movieList.add(model)
                        }
                        movieListAdapter.submitData(PagingData.from(movieList))
                    }

                }else{
                    if(type == MovieDbConstant().movie){
                        viewModel.listData.collect{ it ->
                            movieListAdapter.submitData(it)
                        }
                    }else{
                        tvSeriesViewModel.listData.collect{ it ->
                            movieListAdapter.submitData(it)
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onSliderClick(slider: BaseSliderView?) {
        val model = slider?.bundle?.get("obj") as MovieModel
        model.type = MovieDbConstant().movie
        val intent = Intent(this, MovieDetailActivity::class.java).apply {
            putExtra("obj", model)
        }
        startActivity(intent)
    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onStop() {
        //prevent memory leak
        mainBinding.sliderLayout.stopAutoCycle()
        super.onStop()
    }
}