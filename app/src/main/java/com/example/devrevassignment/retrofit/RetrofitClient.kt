package com.example.devrevassignment.retrofit

import com.example.devrevassignment.api.MovieService
import com.example.devrevassignment.interceptor.MovieInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL="https://api.themoviedb.org/3/"
    const val POSTER_URL="https://image.tmdb.org/t/p/w500"
    const val PROFILE_URL="https://image.tmdb.org/t/p/w185"
    const val BACKDROP_URL="https://image.tmdb.org/t/p/w780"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(
            OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    fun movieService():MovieService= retrofit.create(MovieService::class.java)
}
