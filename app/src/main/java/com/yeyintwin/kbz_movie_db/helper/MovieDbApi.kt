import com.yeyintwin.kbz_movie_db.model.MoviePosterResultModel
import com.yeyintwin.kbz_movie_db.model.MovieResultModel
import com.yeyintwin.kbz_movie_db.model.MovieTrailerResultModel
import com.yeyintwin.kbz_movie_db.model.TvSeriesResultModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET("discover/movie")
    suspend fun getMovie(@Query(value = "page") date:Int) : Response<MovieResultModel>

    @GET("movie/{movieId}/images")
    fun getMoviePoster(@Path(value = "movieId") movieId:Int) : Call<MoviePosterResultModel>

    @GET("movie/{movieId}/videos")
    fun getMovieTrailer(@Path(value = "movieId") movieId:Int) : Call<MovieTrailerResultModel>

    @GET("search/movie")
    suspend fun searchMovie(@Query(value = "query") searchKey:String, @Query(value = "page") date:Int) : Response<MovieResultModel>

    @GET("search/tv")
    suspend fun searchTvSeries(@Query(value = "query") searchKey:String, @Query(value = "page") date:Int) : Response<MovieResultModel>

    @GET("movie/upcoming")
    fun getUpcomingMovie() : Call<MovieResultModel>

    @GET("discover/tv")
    suspend fun getTv(@Query(value = "page") date:Int) : Response<MovieResultModel>

    @GET("tv/{tvId}/images")
    fun getTvSeriesPoster(@Path(value = "tvId") movieId:Int) : Call<MoviePosterResultModel>

    @GET("tv/{tvId}/videos")
    fun getTvSeriesTrailer(@Path(value = "tvId") movieId:Int) : Call<MovieTrailerResultModel>
}