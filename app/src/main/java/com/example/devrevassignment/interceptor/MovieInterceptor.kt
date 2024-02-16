package com.example.devrevassignment.interceptor

import com.example.devrevassignment.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class MovieInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl =chain.request().url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
            .build()

        val request=chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}