package com.example.devrevassignment.viewmodel

import androidx.lifecycle.ViewModel
import com.example.devrevassignment.models.CastData
import com.example.devrevassignment.models.MovieData
import com.example.devrevassignment.models.MovieDetail
import com.example.devrevassignment.status.Resource
import com.example.devrevassignment.usecase.MovieCastUseCase
import com.example.devrevassignment.usecase.MovieDetailUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieDetailViewModel(private val movieDetailUseCase: MovieDetailUseCase,
private val movieCastUseCase: MovieCastUseCase):ViewModel() {

    private val movieDetailStateFlow = MutableStateFlow<Resource<MovieDetail>>(Resource.Empty())
    val movieDetails: StateFlow<Resource<MovieDetail>>
        get() = movieDetailStateFlow

    private val castStateFlow= MutableStateFlow<Resource<List<CastData>>>(Resource.Empty())
    val castList:StateFlow<Resource<List<CastData>>>
        get() = castStateFlow

    var disposable: Disposable? = null

    fun getMovieDetailsById(id:String){
        movieDetailStateFlow.value= Resource.Loading()
        disposable=movieDetailUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ res->
                movieDetailStateFlow.value= Resource.Success(res)
            },
                {
                        throwable->
                    throwable.localizedMessage?.let {
                        movieDetailStateFlow.value= Resource.Error(it)
                    }
                })
    }

    fun getCastList(id:String){
        castStateFlow.value= Resource.Loading()
        disposable=movieCastUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    res->
                castStateFlow.value=Resource.Success(res.cast)
            },
                {
                        throwable->
                    throwable.localizedMessage?.let {
                        castStateFlow.value= Resource.Error(it)
                    }
                }
            )
    }
}