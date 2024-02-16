package com.example.devrevassignment.api

import com.example.devrevassignment.models.MovieCasts
import com.example.devrevassignment.models.MovieDetail
import com.example.devrevassignment.models.Movies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page:Int):Single<Movies>

    @GET("movie/upcoming")
    fun getUpComingMovies(@Query("page") page: Int):Single<Movies>

    @GET("movie/{id}")
    fun getMovieDetail(@Path("id") id:String):Single<MovieDetail>

    @GET("movie/{id}/credits")
    fun getCastList(@Path("id") id: String):Single<MovieCasts>
}