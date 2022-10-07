package com.example.themovieapp.routers

import android.app.Activity
import com.example.themovieapp.activities.MovieDetailActivity
import com.example.themovieapp.activities.MovieSearchActivity

fun Activity.navigateToMovieDetailActivity(movieId: Int) {
    startActivity(MovieDetailActivity.newIntent(this, movieId))
}

fun Activity.navigateToMovieSearchActivity() {
    startActivity(MovieSearchActivity.newIntent(this))
}