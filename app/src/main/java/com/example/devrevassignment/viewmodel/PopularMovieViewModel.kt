package com.example.devrevassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.devrevassignment.models.MovieData
import com.example.devrevassignment.status.Resource
import com.example.devrevassignment.usecase.PopularMovieUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PopularMovieViewModel(private val popularMovieUseCase: PopularMovieUseCase):ViewModel() {

    private val popularStateFlow = MutableStateFlow<Resource<List<MovieData>>>(Resource.Empty())
    private var currentPage = 1
    private var lastPage = 1
    var disposable: Disposable? = null
    val popularMovies:StateFlow<Resource<List<MovieData>>>
        get() = popularStateFlow

    fun getPopularMovies(){
        popularStateFlow.value= Resource.Loading()
        disposable=popularMovieUseCase.execute(currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ res->
                lastPage=res.totalPages
                popularStateFlow.value= Resource.Success(res.results)
            },
                {
                        throwable->
                    lastPage=currentPage
                    throwable.localizedMessage?.let {
                        popularStateFlow.value= Resource.Error(it)
                    }
                })
    }

    fun fetchNextPopularMovies(){
        currentPage++
        getPopularMovies()
    }

    fun refreshPopularMovies(){
        currentPage=1
        getPopularMovies()
    }

    fun isFirstPage():Boolean{
        return currentPage==1
    }

    fun isLastPage():Boolean{
        return currentPage==lastPage
    }
}




