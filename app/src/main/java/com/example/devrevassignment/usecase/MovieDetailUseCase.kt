package com.example.devrevassignment.usecase

import com.example.devrevassignment.models.MovieDetail
import com.example.devrevassignment.repository.MovieRepository
import io.reactivex.Single

class MovieDetailUseCase(private val movieRepository: MovieRepository) {
    fun execute(id:String):Single<MovieDetail>{
        return movieRepository.getMovieDetails(id)
    }
}