package com.example.devrevassignment.viewmodel

import androidx.lifecycle.ViewModel
import com.example.devrevassignment.models.MovieData
import com.example.devrevassignment.status.Resource
import com.example.devrevassignment.usecase.UpComingMovieUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UpComingMovieViewModel(private val upComingMovieUseCase: UpComingMovieUseCase):ViewModel()
{
    private val upComingStateFlow = MutableStateFlow<Resource<List<MovieData>>>(Resource.Empty())
    private var currentPage = 1
    private var lastPage = 1
    var disposable: Disposable? = null
    val upcomingMovies: StateFlow<Resource<List<MovieData>>>
        get() = upComingStateFlow

    fun getUpComingMovies(){
        upComingStateFlow.value= Resource.Loading()
        disposable=upComingMovieUseCase.execute(currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ res->
                lastPage=res.totalPages
                upComingStateFlow.value= Resource.Success(res.results)
            },
                {
                        throwable->
                    lastPage=currentPage
                    throwable.localizedMessage?.let {
                        upComingStateFlow.value= Resource.Error(it)
                    }
                })
    }

    fun fetchNextUpComingMovies(){
        currentPage++
        getUpComingMovies()
    }

    fun refreshUpComingMovies(){
        currentPage=1
        getUpComingMovies()
    }

    fun isFirstPage():Boolean{
        return currentPage==1
    }

    fun isLastPage():Boolean{
        return currentPage==lastPage
    }
}