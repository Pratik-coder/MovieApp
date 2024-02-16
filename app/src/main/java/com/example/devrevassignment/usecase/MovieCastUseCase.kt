package com.example.devrevassignment.usecase

import com.example.devrevassignment.models.MovieCasts
import com.example.devrevassignment.repository.MovieRepository
import io.reactivex.Single

class MovieCastUseCase(private val movieRepository: MovieRepository) {
    fun execute(id:String):Single<MovieCasts>{
        return movieRepository.getMovieCasts(id)
    }
}