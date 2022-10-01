package com.example.themovieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapp.R
import com.example.themovieapp.adapters.BannerAdapter
import com.example.themovieapp.adapters.ShowcaseAdapter
import com.example.themovieapp.data.models.MovieModel
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.data.vos.GenreVO
import com.example.themovieapp.delegates.BannerViewHolderDelegate
import com.example.themovieapp.delegates.MovieViewHolderDelegate
import com.example.themovieapp.delegates.ShowcaseViewHolderDelegate
import com.example.themovieapp.dummy.dummyGenreList
import com.example.themovieapp.viewpods.ActorListViewPod
import com.example.themovieapp.viewpods.MovieListViewPod
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BannerViewHolderDelegate, ShowcaseViewHolderDelegate, MovieViewHolderDelegate {

    lateinit var mBannerAdapter : BannerAdapter
    lateinit var mShowcaseAdapter : ShowcaseAdapter

    lateinit var mBestPopularMovieListViewPod : MovieListViewPod
    lateinit var mMoviesByGenreViewPod: MovieListViewPod
    lateinit var mActorListViewPod: ActorListViewPod

    private val mMovieModel: MovieModel = MovieModelImpl

    private var mGenres: List<GenreVO>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpToolbar()
        setUpViewPods()
        setUpBannerViewPager()
        setUpShowcaseRecyclerView()
        setUpListeners()

        requestData()
    }

    private fun requestData() {
        // Now Playing Movies
        mMovieModel.getNowPlayingMovies {
            showError(it)
        }?.observe(this) {
            mBannerAdapter.setNewData(it)
        }

        // Popular Movies
        mMovieModel.getPopularMovies {
            showError(it)
        }?.observe(this) {
            mBestPopularMovieListViewPod.setData(it)
        }

        // Top Rated Movies
        mMovieModel.getTopRatedMovies {
            showError(it)
        }?.observe(this) {
            mShowcaseAdapter.setNewData(it)
        }

        // Genre List
        mMovieModel.getGenre(
            onSuccess = {

                mGenres = it

                setUpGenreTabLayout(it)

                it.firstOrNull()?.id?.let { genreId ->
                    getMoviesById(genreId)
                }
            },
            onFailure = {
                showError(it)
            }
        )

        // Actor
        mMovieModel.getActors(
            onSuccess = {
                mActorListViewPod.setData(it)
            },
            onFailure = {
                showError(it)
            }
        )
    }

    private fun getMoviesById(genreId: Int) {
        mMovieModel.getMoviesByGenre(
            genreId = genreId.toString(),
            onSuccess = {
                mMoviesByGenreViewPod.setData(it)
            },
            onFailure = {
                showError(it)
            }
        )
    }

    private fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun setUpViewPods() {
        mBestPopularMovieListViewPod = vpBestPopularMovieList as MovieListViewPod
        mMoviesByGenreViewPod = vpMoviesByGenre as MovieListViewPod
        mActorListViewPod = vpActorsHomeScreen as ActorListViewPod

        mBestPopularMovieListViewPod.setUpMovieListViewPod(this)
        mMoviesByGenreViewPod.setUpMovieListViewPod(this)
    }

    private fun setUpShowcaseRecyclerView() {
        mShowcaseAdapter = ShowcaseAdapter(this)
        rvShowcases.adapter = mShowcaseAdapter
        rvShowcases.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpListeners() {
        //Genre Tab Layout
        tabLayoutGenre.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mGenres?.get(tab?.position ?: 0)?.id?.let {
                    getMoviesById(it)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setUpGenreTabLayout(genreList: List<GenreVO>) {
        genreList.forEach {
            tabLayoutGenre.newTab().apply {
                text = it.name
                tabLayoutGenre.addTab(this)
            }
        }
    }

    private fun setUpBannerViewPager() {
        mBannerAdapter = BannerAdapter(this)
        viewPagerBanner.adapter = mBannerAdapter

        dotsIndicatorBanner.attachTo(viewPagerBanner)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_discover, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                startActivity(MovieSearchActivity.newIntent(this))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTapMovieFromBanner(movieId: Int) {
        startActivity(MovieDetailActivity.newIntent(this, movieId))
    }

    override fun onTapMovieFromShowcase(movieId: Int) {
        startActivity(MovieDetailActivity.newIntent(this, movieId))
    }

    override fun onTapMovie(movieId: Int) {
        startActivity(MovieDetailActivity.newIntent(this, movieId))
    }
}