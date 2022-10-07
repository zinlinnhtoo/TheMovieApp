package com.example.themovieapp.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.mvp.views.MovieDetailView

class MovieDetailPresenterImpl: ViewModel(), MovieDetailPresenter {
    private val mMovieModel = MovieModelImpl
    private var mView: MovieDetailView? = null

    override fun initView(view: MovieDetailView) {
        mView = view
    }

    override fun onUiReadyInMovieDetail(owner: LifecycleOwner, movieId: Int) {
        mMovieModel.getMovieDetails(movieId.toString()) {
            mView?.showError(it)
        }?.observe(owner) {
            it?.let {
                mView?.showMovieDetails(it)
            }
        }

        mMovieModel.getCreditsByMovie(movieId.toString(),
        onSuccess =  {
            mView?.showCreditsByMovie(it.first, it.second)
        },
        onFailure = {
            mView?.showError(it)
        })
    }

    override fun onTapBack() {
        mView?.navigateBack()
    }

    override fun onUiReady(owner: LifecycleOwner) {
    }
}