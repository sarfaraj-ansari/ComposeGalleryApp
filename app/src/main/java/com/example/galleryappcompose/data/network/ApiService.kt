package com.example.galleryappcompose.data.network

import com.example.galleryappcompose.data.models.PhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("v1/curated")
    suspend fun getAllPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int): PhotoResponse

    @GET("v1/search")
    suspend fun getSearchedPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int, @Query("query") query: String): PhotoResponse

    @GET("videos/popular")
    suspend fun getPopularVideos(@Query("page") page: Int, @Query("per_page") perPage: Int): PhotoResponse

    @GET("videos/search")
    suspend fun getSearchedVideos(@Query("page") page: Int, @Query("per_page") perPage: Int, @Query("query") query: String): PhotoResponse

    @GET("/v1/photos/{id}")
    suspend fun getImageDetails(@Path("id") id: Int): Response<PhotoResponse>
}