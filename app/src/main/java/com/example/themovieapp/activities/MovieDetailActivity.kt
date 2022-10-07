package com.example.themovieapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.themovieapp.R
import com.example.themovieapp.data.models.MovieModel
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.data.vos.ActorVO
import com.example.themovieapp.data.vos.GenreVO
import com.example.themovieapp.data.vos.MovieVO
import com.example.themovieapp.mvp.presenters.IBasePresenter
import com.example.themovieapp.mvp.presenters.MovieDetailPresenter
import com.example.themovieapp.mvp.presenters.MovieDetailPresenterImpl
import com.example.themovieapp.mvp.views.MovieDetailView
import com.example.themovieapp.utils.IMAGE_BASE_URL
import com.example.themovieapp.viewpods.ActorListViewPod
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.view_holder_movie.*

class MovieDetailActivity : AppCompatActivity(), MovieDetailView{

    companion object {

        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun newIntent(context: Context, movieId: Int): Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }

    //View Pods
    lateinit var actorsViewPod: ActorListViewPod
    lateinit var creatorsViewPod: ActorListViewPod

    private lateinit var mPresenter: MovieDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        setUpPresenter()

        setUpViewPods()
        setUpListeners()

        val movieId = intent?.getIntExtra(EXTRA_MOVIE_ID, 0)
        movieId?.let {
            mPresenter.onUiReadyInMovieDetail(this, movieId)
        }
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[MovieDetailPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListeners() {
        btnBack.setOnClickListener { super.onBackPressed() }
    }

    private fun setUpViewPods() {
        actorsViewPod = vpActors as ActorListViewPod
        creatorsViewPod = vpCreators as ActorListViewPod

        actorsViewPod.setUpActorViewPod(
            backgroundColorReference = R.color.colorPrimary,
            titleText = getString(R.string.lbl_actors),
            moreTitleText = ""
        )

        creatorsViewPod.setUpActorViewPod(
            backgroundColorReference = R.color.colorPrimary,
            titleText = getString(R.string.lbl_creators),
            moreTitleText = getString(R.string.lbl_more_creators)
        )
    }

    private fun bindData(movie: MovieVO) {
        Glide.with(this)
            .load("$IMAGE_BASE_URL${movie.posterPath}")
            .into(ivMovieDetail)
        tvMovieNameDetail.text = movie.title ?: ""
        tvMovieReleaseYear.text = movie.releaseDate?.substring(0, 4)
        tvRating.text = movie.voteAverage?.toString() ?: ""
        movie.voteCount?.let {
            tvNumberOfVotes.text = "$it VOTES"
        }
        rbRatingDetail.rating = movie.getRatingBasedOnFiveStars()

        bindGenres(movie, movie.genres ?: listOf())

        tvOverview.text = movie.overView ?: ""
        tvOriginalTitle.text = movie.title ?: ""
        tvType.text = movie.getGenresAsCommaSeparatedString()
        tvProduction.text = movie.getProductionCountriesAsCommaSeparatedString()
        tvPremiere.text = movie.releaseDate ?: ""
        tvDescription.text = movie.overView ?: ""
    }

    private fun bindGenres(movie: MovieVO, genres: List<GenreVO>) {
        movie.genres?.count()?.let {
            tvFirstGenre.text = genres.firstOrNull()?.name ?: ""
            tvSecondGenre.text = genres.getOrNull(1)?.name ?: ""
            tvThirdGenre.text = genres.getOrNull(2)?.name ?: ""

            if (it < 3) {
                tvThirdGenre.visibility = View.GONE
            } else if (it < 2) {
                tvSecondGenre.visibility = View.GONE
            }
        }
    }

    override fun showMovieDetails(movie: MovieVO) {
        bindData(movie)
    }

    override fun showCreditsByMovie(cast: List<ActorVO>, crew: List<ActorVO>) {
        actorsViewPod.setData(cast)
        creatorsViewPod.setData(crew)
    }

    override fun navigateBack() {
        finish()
    }

    override fun showError(errorString: String) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
    }

}