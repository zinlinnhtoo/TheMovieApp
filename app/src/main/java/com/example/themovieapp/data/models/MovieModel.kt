package com.example.themovieapp.data.models

import com.example.themovieapp.data.vos.MovieVO

interface MovieModel {
    fun getNowPlayingMovies(
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    )
}