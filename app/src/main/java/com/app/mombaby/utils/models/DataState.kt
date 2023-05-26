package com.app.mombaby.utils.models

sealed class DataState<out T> {
    data class Success<out R>(val data: R) : DataState<R>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    data class RetrofitError(val errorBody: String?) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}