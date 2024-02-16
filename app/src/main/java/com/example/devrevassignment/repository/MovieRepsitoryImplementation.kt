package com.example.devrevassignment.repository

import com.example.devrevassignment.models.MovieCasts
import com.example.devrevassignment.models.MovieDetail
import com.example.devrevassignment.models.Movies
import com.example.devrevassignment.retrofit.RetrofitClient
import io.reactivex.Single

class MovieRepsitoryImplementation:MovieRepository {
    override fun getPopularMovies(page: Int): Single<Movies> {
        return RetrofitClient.movieService().getPopularMovies(page)
    }

    override fun getUpComingMovies(page: Int): Single<Movies> {
        return RetrofitClient.movieService().getUpComingMovies(page)
    }

    override fun getMovieDetails(id: String): Single<MovieDetail> {
       return RetrofitClient.movieService().getMovieDetail(id)
    }

    override fun getMovieCasts(id: String): Single<MovieCasts> {
        return RetrofitClient.movieService().getCastList(id)
    }
}