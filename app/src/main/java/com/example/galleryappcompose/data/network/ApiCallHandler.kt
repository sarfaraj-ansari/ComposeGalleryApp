package com.example.galleryappcompose.data.network

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ApiCallHandler {

    suspend fun <T> makeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response: Response<T> = apiCall.invoke()
            if (response.isSuccessful && response.body() != null) {
                println("executed            1")
                println("executed            ${response.message()}  code ${response.code()}")
                NetworkResult.Success(response.body()!!)
            } else {
                println("executed            2")
                println("executed            ${response.message()}  code ${response.code()}")
                NetworkResult.Failure(response.message(), response.code())
            }
        } catch (e: Exception) {
            when(e) {
                is HttpException -> {
                    println("executed            3")
                    println("executed            ${e.message()}  code ${e.code()}")
                    NetworkResult.Failure(e.message(), e.code())
                }
                is UnknownHostException -> {
                    println("executed            4")
                    println("executed            Network Error")
                    NetworkResult.Failure("Network Error", 5)
                }
                is ConnectException -> {
                    println("executed            5")
                    println("executed            Slow Network Error")
                    NetworkResult.Failure("Slow Network Error", 5)
                }
                is SocketTimeoutException -> {
                    println("executed            6")
                    println("executed            Slow Network Error")
                    NetworkResult.Failure("Slow Network Error", 5)
                }
                is IOException -> {
                    println("executed            7")
                    println("executed            Slow Network Error")
                    NetworkResult.Failure("Slow Network Error", 5)
                }
                else -> {
                    println("executed            8")
                    println("executed            ${e.message}")
                    NetworkResult.Failure(e.message, null)
                }
            }
        }
    }

}