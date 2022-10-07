package com.example.themovieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapp.R
import com.example.themovieapp.adapters.BannerAdapter
import com.example.themovieapp.adapters.ShowcaseAdapter
import com.example.themovieapp.data.models.MovieModel
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.data.vos.ActorVO
import com.example.themovieapp.data.vos.GenreVO
import com.example.themovieapp.data.vos.MovieVO
import com.example.themovieapp.delegates.BannerViewHolderDelegate
import com.example.themovieapp.delegates.MovieViewHolderDelegate
import com.example.themovieapp.delegates.ShowcaseViewHolderDelegate
import com.example.themovieapp.dummy.dummyGenreList
import com.example.themovieapp.mvp.presenters.MainPresenter
import com.example.themovieapp.mvp.presenters.MainPresenterImpl
import com.example.themovieapp.mvp.views.MainView
import com.example.themovieapp.viewpods.ActorListViewPod
import com.example.themovieapp.viewpods.MovieListViewPod
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    MainView {

    lateinit var mBannerAdapter : BannerAdapter
    lateinit var mShowcaseAdapter : ShowcaseAdapter

    lateinit var mBestPopularMovieListViewPod : MovieListViewPod
    lateinit var mMoviesByGenreViewPod: MovieListViewPod
    lateinit var mActorListViewPod: ActorListViewPod

    //presenter
    private lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpPresenter()

        setUpToolbar()
        setUpViewPods()
        setUpBannerViewPager()
        setUpShowcaseRecyclerView()
        setUpListeners()

        mPresenter.onUiReady(this)

    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[MainPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpViewPods() {
        mBestPopularMovieListViewPod = vpBestPopularMovieList as MovieListViewPod
        mMoviesByGenreViewPod = vpMoviesByGenre as MovieListViewPod
        mActorListViewPod = vpActorsHomeScreen as ActorListViewPod

        mBestPopularMovieListViewPod.setUpMovieListViewPod(mPresenter)
        mMoviesByGenreViewPod.setUpMovieListViewPod(mPresenter)
    }

    private fun setUpShowcaseRecyclerView() {
        mShowcaseAdapter = ShowcaseAdapter(mPresenter)
        rvShowcases.adapter = mShowcaseAdapter
        rvShowcases.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpListeners() {
        //Genre Tab Layout
        tabLayoutGenre.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mPresenter.onTapGenre(tab?.position ?: 0)
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
        mBannerAdapter = BannerAdapter(mPresenter)
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

    override fun showNowPlayingMovies(nowPlayingMovies: List<MovieVO>) {
        mBannerAdapter.setNewData(nowPlayingMovies)
    }

    override fun showPopularMovies(popularMovies: List<MovieVO>) {
        mBestPopularMovieListViewPod.setData(popularMovies)
    }

    override fun showTopRatedMovies(topRatedMovies: List<MovieVO>) {
        mShowcaseAdapter.setNewData(topRatedMovies)
    }

    override fun showGenres(genreList: List<GenreVO>) {
        setUpGenreTabLayout(genreList)
    }

    override fun showMoviesByGenre(moviesByGenre: List<MovieVO>) {
        mMoviesByGenreViewPod.setData(moviesByGenre)
    }

    override fun showActors(actors: List<ActorVO>) {
        mActorListViewPod.setData(actors)
    }

    override fun navigateToMovieDetailScreen(movieId: Int) {
        startActivity(MovieDetailActivity.newIntent(this, movieId))
    }

    override fun showError(errorString: String) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
    }
}