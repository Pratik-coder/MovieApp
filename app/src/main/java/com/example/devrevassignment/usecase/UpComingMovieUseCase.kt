package com.example.devrevassignment.usecase

import com.example.devrevassignment.models.Movies
import com.example.devrevassignment.repository.MovieRepository
import io.reactivex.Single

class UpComingMovieUseCase(private val movieRepository: MovieRepository) {
    fun execute(page:Int):Single<Movies>{
        return movieRepository.getUpComingMovies(page)
    }
}