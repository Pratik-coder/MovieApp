package com.example.devrevassignment.status

class Resource<T>(val status:Status,val data:T?,val message:String?) {

    enum class Status{
        SUCCESS,ERROR,LOADING,EMPTY
    }

    companion object{
        fun<T> Success(data:T?):Resource<T>{
            return Resource(Status.SUCCESS,data,null)
        }

        fun<T> Error(message: String?):Resource<T>{
            return Resource(Status.ERROR,null,message)
        }

        fun<T> Loading():Resource<T>{
            return Resource(Status.LOADING,null,null)
        }

        fun<T> Empty():Resource<T>{
            return Resource(Status.EMPTY,null,null)
        }
    }
}