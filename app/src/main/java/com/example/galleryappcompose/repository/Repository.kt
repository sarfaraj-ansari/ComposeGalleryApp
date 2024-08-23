package com.example.galleryappcompose.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.galleryappcompose.data.datasource.PhotoResource
import com.example.galleryappcompose.data.datasource.SearchedPhotoSource
import com.example.galleryappcompose.data.datasource.SearchedVideoSource
import com.example.galleryappcompose.data.datasource.VideoResource
import com.example.galleryappcompose.data.models.PhotoResponse
import com.example.galleryappcompose.data.network.ApiCallHandler
import com.example.galleryappcompose.data.network.ApiService
import com.example.galleryappcompose.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) : BaseRepo,
    ApiCallHandler {
    override fun getAllPhotos(): Flow<PagingData<PhotoResponse.Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                maxSize = 100
            ),
            pagingSourceFactory = { PhotoResource(apiService) }
        ).flow
    }

    override fun getSearchedPhotos(query: String): Flow<PagingData<PhotoResponse.Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                maxSize = 100
            ),
            pagingSourceFactory = { SearchedPhotoSource(query = query, apiService = apiService) }
        ).flow
    }

    override fun getPopularVideos(): Flow<PagingData<PhotoResponse.Video>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                maxSize = 100
            ),
            pagingSourceFactory = { VideoResource(apiService) }
        ).flow
    }

    override fun getSearchedVideos(query: String): Flow<PagingData<PhotoResponse.Video>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                maxSize = 100
            ),
            pagingSourceFactory = { SearchedVideoSource(apiService, query) }
        ).flow
    }

    override suspend fun getImageDetails(): NetworkResult<PhotoResponse> {
        return makeApiCall(apiCall = { apiService.getImageDetails(2014422) })
    }

}