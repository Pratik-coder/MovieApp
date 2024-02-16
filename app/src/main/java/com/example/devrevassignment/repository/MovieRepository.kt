package com.example.devrevassignment.repository

import com.example.devrevassignment.models.MovieCasts
import com.example.devrevassignment.models.MovieDetail
import com.example.devrevassignment.models.Movies
import io.reactivex.Single

interface MovieRepository {

    fun getPopularMovies(page:Int):Single<Movies>
    fun getUpComingMovies(page: Int):Single<Movies>
    fun getMovieDetails(id:String):Single<MovieDetail>
    fun getMovieCasts(id:String):Single<MovieCasts>

}