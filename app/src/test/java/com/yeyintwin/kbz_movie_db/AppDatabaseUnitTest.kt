package com.yeyintwin.kbz_movie_db
import androidx.test.platform.app.InstrumentationRegistry
import com.yeyintwin.kbz_movie_db.dao.MovieDao
import com.yeyintwin.kbz_movie_db.database.AppDataBase
import com.yeyintwin.kbz_movie_db.model.MovieModel
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class AppDatabaseUnitTest: TestCase() {

    private lateinit var appDataBase: AppDataBase
    private lateinit var movieDao: MovieDao


    @Before
    override public fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        appDataBase = AppDataBase.getDatabase(context)
        movieDao = appDataBase.movieDao()
        super.setUp()
    }

    @After
    fun closeDb(){
        appDataBase.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `testInsertAndReadMovie`() = kotlinx.coroutines.test.runTest {
        var model = MovieModel(id = 0, false, "/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg", "en","Avatar: The Way of Water",
            "Set more than a decade after the events of the first film, learn the story of the Sully family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure.",
            5896.522, "/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", "2022-12-14", "Avatar: The Way of Water",
            false, 7.7, 3251, "", "", "Movie")

        //insert movie
        movieDao.addItem(model)

        //get movie
        val list = movieDao.getAll()
        assert(list.contains(model))
    }
}