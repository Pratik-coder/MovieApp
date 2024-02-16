package com.example.devrevassignment.modules

import com.example.devrevassignment.adapter.MovieListAdapter
import com.example.devrevassignment.repository.MovieRepository
import com.example.devrevassignment.repository.MovieRepsitoryImplementation
import com.example.devrevassignment.usecase.MovieCastUseCase
import com.example.devrevassignment.usecase.MovieDetailUseCase
import com.example.devrevassignment.usecase.PopularMovieUseCase
import com.example.devrevassignment.usecase.UpComingMovieUseCase
import com.example.devrevassignment.viewmodel.MovieDetailViewModel
import com.example.devrevassignment.viewmodel.PopularMovieViewModel
import com.example.devrevassignment.viewmodel.UpComingMovieViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule= module {
    factory<MovieRepository>{MovieRepsitoryImplementation()}
    factory{MovieListAdapter(androidContext())}
}

val popularModule= module {
    factory { PopularMovieUseCase(get())}
    viewModel { PopularMovieViewModel(get())}
}

val upcomingModule= module {
    factory {UpComingMovieUseCase(get())}
    viewModel { UpComingMovieViewModel(get())}
}

val movieDetailModule =module {
    factory {MovieDetailUseCase(get())}
    factory { MovieCastUseCase(get())}
    viewModel { MovieDetailViewModel(get(),get())}
}
