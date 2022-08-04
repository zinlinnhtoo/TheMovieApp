package com.example.themovieapp.network

import com.example.themovieapp.network.responses.GetGenreResponse
import com.example.themovieapp.network.responses.MovieListResponse
import com.example.themovieapp.utils.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieApi {
    @GET(API_GET_NOW_PLAYING)
    fun getNowPlayingMovies(
        @Query(PARAM_API_KEY) apiKey: String = MOVIE_API_KEY,
        @Query(PARAM_PAGE) page: Int = 1
    ): Call<MovieListResponse>

    @GET(API_GET_POPULAR_MOVIES)
    fun getPopularMovies(
        @Query(PARAM_API_KEY) apiKey: String = MOVIE_API_KEY,
        @Query(PARAM_PAGE) page: Int = 1
    ): Call<MovieListResponse>

    @GET(API_GET_TOP_RATED_MOVIES)
    fun getTopRatedMovies(
        @Query(PARAM_API_KEY) apiKey: String = MOVIE_API_KEY,
        @Query(PARAM_PAGE) page: Int = 1
    ): Call<MovieListResponse>

    @GET(API_GET_GENRE)
    fun getGenres(
        @Query(PARAM_API_KEY) apiKey: String = MOVIE_API_KEY
    ): Call<GetGenreResponse>

    @GET(API_GET_MOVIES_BY_GENRE)
    fun getMoviesByGenre(
        @Query(PARAM_API_KEY) apiKey: String = MOVIE_API_KEY,
        @Query(PARAM_GENRE_ID) genreId: String
    ): Call<MovieListResponse>
}