package com.example.galleryappcompose.data.network

import android.content.SharedPreferences
import dagger.hilt.EntryPoint
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class Interceptor @Inject constructor(private val sharedPreferences: SharedPreferences): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("Accept", "application/json")
        request.addHeader("Authorization", "NZ64I6FcIOE7bifjxX8Je20kcLkBy3JVxg6hPps3VHIyJkkS7KrkWO31" /*sharedPreferences.getString("token", "") ?: ""*/)
        return chain.proceed(request.build())
    }
}