package com.example.themovieapp.network

import com.example.themovieapp.network.responses.MovieListResponse
import com.example.themovieapp.utils.API_GET_NOW_PLAYING
import com.example.themovieapp.utils.MOVIE_API_KEY
import com.example.themovieapp.utils.PARAM_API_KEY
import com.example.themovieapp.utils.PARAM_PAGE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieApi {
    @GET(API_GET_NOW_PLAYING)
    fun getNowPlayingMovies(
        @Query(PARAM_API_KEY) apiKey: String = MOVIE_API_KEY,
        @Query(PARAM_PAGE) page: Int = 1
    ): Call<MovieListResponse>
}