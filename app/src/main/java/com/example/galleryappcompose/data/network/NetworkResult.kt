package com.example.galleryappcompose.data.network

sealed class NetworkResult<out R> {
    data object Loading : NetworkResult<Nothing>()
    data class Success<out T>(val data: T): NetworkResult<T>()
    data class Failure(val message: String?, val errorCode: Int?): NetworkResult<Nothing>()
}