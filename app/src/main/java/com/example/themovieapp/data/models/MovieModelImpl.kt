package com.example.themovieapp.data.models

import com.example.themovieapp.data.vos.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

object MovieModelImpl : BaseModel(),MovieModel {


    override fun getNowPlayingMovies(
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        //database
        onSuccess(mMovieDatabase?.movieDao()?.getMoviesByType(type = NOW_PLAYING) ?: listOf())

        mMovieApi.getNowPlayingMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.results?.forEach { movie -> movie.type = NOW_PLAYING }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

                onSuccess(it.results ?: listOf())
            }, {
                onFailure(it.localizedMessage.orEmpty())
            })
    }

    override fun getPopularMovies(onSuccess: (List<MovieVO>) -> Unit, onFailure: (String) -> Unit) {
        //database
        onSuccess(mMovieDatabase?.movieDao()?.getMoviesByType(type = POPULAR) ?: listOf())

        mMovieApi.getPopularMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.results?.forEach { movie -> movie.type = POPULAR }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

                onSuccess(it.results ?: listOf())
            }, {
                onFailure(it.localizedMessage.orEmpty())
            })
    }

    override fun getTopRatedMovies(
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        //database
        onSuccess(mMovieDatabase?.movieDao()?.getMoviesByType(type = TOP_RATED) ?: listOf())

        mMovieApi.getPopularMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.results?.forEach { movie -> movie.type = TOP_RATED }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

                onSuccess(it.results ?: listOf())
            }, {
                onFailure(it.localizedMessage.orEmpty())
            })
    }

    override fun getGenre(onSuccess: (List<GenreVO>) -> Unit, onFailure: (String) -> Unit) {
       mMovieApi.getGenres()
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe({
               onSuccess(it.genres ?: listOf())
           }, {
               onFailure(it.localizedMessage.orEmpty())
           })
    }

    override fun getMoviesByGenre(
        genreId: String,
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mMovieApi.getMoviesByGenre(genreId = genreId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it.results ?: listOf())
            }, {
                onFailure(it.localizedMessage.orEmpty())
            })
    }

    override fun getActors(onSuccess: (List<ActorVO>) -> Unit, onFailure: (String) -> Unit) {
        mMovieApi.getActors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it.results ?: listOf())
            }, {
                onFailure(it.localizedMessage.orEmpty())
            })
    }

    override fun getMovieDetails(
        movieId: String,
        onSuccess: (MovieVO) -> Unit,
        onFailure: (String) -> Unit
    ) {
        //database
        val movieFromDatabase = mMovieDatabase?.movieDao()?.getMovieById(movieId = movieId.toInt())
        movieFromDatabase?.let(onSuccess)

        mMovieApi.getMovieDetail(movieId = movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val movieFromDatabaseToSync = mMovieDatabase?.movieDao()?.getMovieById(movieId = movieId.toInt())
                it.type = movieFromDatabaseToSync?.type
                mMovieDatabase?.movieDao()?.insertSingleMovie(it)

                onSuccess(it)
            }, {
                onFailure(it.localizedMessage.orEmpty())
            })
    }

    override fun getCreditsByMovie(
        movieId: String,
        onSuccess: (Pair<List<ActorVO>, List<ActorVO>>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mMovieApi.getCreditsByMovie(movieId = movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(Pair(it.cast ?: listOf(), it.crew ?: listOf()))
            }, {
                onFailure(it.localizedMessage.orEmpty())
            })
    }
}