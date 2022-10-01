package com.example.themovieapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themovieapp.R
import com.example.themovieapp.adapters.MovieAdapter
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.delegates.MovieViewHolderDelegate
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_search.*
import java.util.concurrent.TimeUnit

class MovieSearchActivity : AppCompatActivity(), MovieViewHolderDelegate {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MovieSearchActivity::class.java)
        }
    }

    private lateinit var mMovieAdapter: MovieAdapter

    private val mMovieModel = MovieModelImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)

        setUpRecyclerView()
        setUpListener()
    }

    private fun setUpListener() {
        etSearch.textChanges()
            .debounce(500L, TimeUnit.MILLISECONDS)
            .flatMap { mMovieModel.searchMovie(it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mMovieAdapter.setNewData(it)
            }, {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            })
    }

    private fun setUpRecyclerView() {
        mMovieAdapter = MovieAdapter(this)
        rvMovies.adapter = mMovieAdapter
        rvMovies.layoutManager = GridLayoutManager(this, 2)
    }

    override fun onTapMovie(movieId: Int) {

    }
}