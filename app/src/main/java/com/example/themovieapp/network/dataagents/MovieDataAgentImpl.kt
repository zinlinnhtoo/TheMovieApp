//package com.example.themovieapp.network.dataagents
//
//import android.os.AsyncTask
//import android.util.Log
//import com.example.themovieapp.network.responses.MovieListResponse
//import com.example.themovieapp.utils.API_GET_NOW_PLAYING
//import com.example.themovieapp.utils.BASE_URL
//import com.example.themovieapp.utils.MOVIE_API_KEY
//import com.google.gson.Gson
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//
//object MovieDataAgentImpl: MovieDataAgent {
//    override fun getNowPlayingMovies() {
//        GetNowPlayingMovieTask().execute()
//    }
//
//    class GetNowPlayingMovieTask(): AsyncTask<Void, Void, MovieListResponse>() {
//        override fun doInBackground(vararg params: Void?): MovieListResponse? {
//            val url: URL
//            var reader: BufferedReader? = null
//            val stringBuilder: StringBuilder
//
//            try {
//                url = URL(
//                    """$BASE_URL$API_GET_NOW_PLAYING?api_key=$MOVIE_API_KEY&language=en-US&page=1"""
//                )
//
//                val connection = url.openConnection() as HttpURLConnection
//
//
//                connection.requestMethod = "GET"
//                connection.readTimeout = 15 * 1000
//                connection.doInput = true
//                connection.doOutput = false
//                connection.connect()
//
//
//                reader = BufferedReader(
//                    InputStreamReader(connection.inputStream)
//                )
//                stringBuilder = StringBuilder()
//
//                for (line in reader.readLines()) {
//                   stringBuilder.append(line + "\n")
//                }
//
//                val responseString = stringBuilder.toString()
//                Log.d("NowPlayingMovie", responseString)
//
//                val movieListResponse = Gson().fromJson(
//                    responseString,
//                    MovieListResponse::class.java
//                )
//
//                return movieListResponse
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.e("NewsError", e.message ?: "")
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close()
//                    } catch (ioe: IOException) {
//                        ioe.printStackTrace()
//                    }
//                }
//            }
//            return null
//        }
//
//        override fun onPostExecute(result: MovieListResponse?) {
//            super.onPostExecute(result)
//        }
//    }
//}