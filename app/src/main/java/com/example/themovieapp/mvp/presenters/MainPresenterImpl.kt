package com.example.themovieapp.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.themovieapp.data.models.MovieModel
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.data.vos.GenreVO
import com.example.themovieapp.mvp.views.MainView

class MainPresenterImpl: ViewModel(), MainPresenter {
    var mView: MainView? = null
    private val mMovieModel: MovieModel = MovieModelImpl
    //States
    private var mGenres: List<GenreVO>? = listOf()

    override fun initView(view: MainView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        mMovieModel.getNowPlayingMovies {
            mView?.showError(it)
        }?.observe(owner) {
            mView?.showNowPlayingMovies(it)
        }

        mMovieModel.getPopularMovies {
            mView?.showError(it)
        }?.observe(owner) {
            mView?.showPopularMovies(it)
        }

        mMovieModel.getTopRatedMovies {
            mView?.showError(it)
        }?.observe(owner) {
            mView?.showTopRatedMovies(it)
        }

        mMovieModel.getGenre(
            onSuccess = {
                mGenres = it
                mView?.showGenres(it)
                it.firstOrNull()?.id?.let { firstGenreId ->
                    onTapGenre(firstGenreId)
                }
            },
            onFailure = {
                mView?.showError(it)
            }
        )

        mMovieModel.getActors(
            onSuccess =  {
                mView?.showActors(it)
            },
            onFailure = {
                mView?.showError(it)
            }
        )
    }

    override fun onTapGenre(genrePosition: Int) {
        mGenres?.getOrNull(genrePosition)?.id?.let { genreId ->
            mMovieModel.getMoviesByGenre(genreId = genreId.toString(), onSuccess = {
                mView?.showMoviesByGenre(it)
            },
            onFailure =  {
                mView?.showError(it)
            })
        }
    }

    override fun onTapMovieFromBanner(movieId: Int) {
        mView?.navigateToMovieDetailScreen(movieId)
    }

    override fun onTapMovie(movieId: Int) {
        mView?.navigateToMovieDetailScreen(movieId)
    }

    override fun onTapMovieFromShowcase(movieId: Int) {
        mView?.navigateToMovieDetailScreen(movieId)
    }
}