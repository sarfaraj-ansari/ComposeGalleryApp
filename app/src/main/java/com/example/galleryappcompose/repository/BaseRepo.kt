package com.example.galleryappcompose.repository

import androidx.paging.PagingData
import com.example.galleryappcompose.data.models.PhotoResponse
import com.example.galleryappcompose.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BaseRepo {
    fun getAllPhotos() : Flow<PagingData<PhotoResponse.Photo>>

    fun getSearchedPhotos(query: String) : Flow<PagingData<PhotoResponse.Photo>>

    fun getPopularVideos() : Flow<PagingData<PhotoResponse.Video>>

    fun getSearchedVideos(query: String) : Flow<PagingData<PhotoResponse.Video>>

    suspend fun getImageDetails() :NetworkResult<PhotoResponse>
}