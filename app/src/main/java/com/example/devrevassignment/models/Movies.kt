package com.example.devrevassignment.models

import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("page") val page:Int,
    @SerializedName("results") val results:List<MovieData>,
    @SerializedName("total_pages") val totalPages:Int,
    @SerializedName("total_results") val totalResults:Int,
)

data class MovieData(
    @SerializedName("adult") val adult:Boolean,
    @SerializedName("backdrop_path") val backdropPath:Boolean,
    @SerializedName("genre_ids") val genreIds:List<Int>,
    @SerializedName("id") val id:Int,
    @SerializedName("original_language") val originalLanguage:String,
    @SerializedName("original_title") val originalTitle:String,
    @SerializedName("overview") val overview:String,
    @SerializedName("popularity") val popularity:Double,
    @SerializedName("poster_path") val posterPath:String,
    @SerializedName("release_date") val relaeseDate:String,
    @SerializedName("title") val title:String,
    @SerializedName("video") val video:Boolean,
    @SerializedName("vote_average") val voteAverage:Double,
    @SerializedName("vote_count") val voteCount:Int,
)
