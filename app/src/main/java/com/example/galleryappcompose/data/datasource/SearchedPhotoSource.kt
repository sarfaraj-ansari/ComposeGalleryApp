package com.example.galleryappcompose.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.galleryappcompose.data.models.PhotoResponse
import com.example.galleryappcompose.data.network.ApiService
import kotlinx.coroutines.delay

class SearchedPhotoSource(private val query: String, private val apiService: ApiService) :
    PagingSource<Int, PhotoResponse.Photo>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoResponse.Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage: LoadResult.Page<Int, PhotoResponse.Photo>? =
                state.closestPageToPosition(anchorPosition)
            anchorPage?.nextKey?.minus(1) ?: anchorPage?.prevKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse.Photo> {
        return try {
            val curPage = params.key ?: 1
            val response = apiService.getSearchedPhotos(curPage, params.loadSize, query)
            LoadResult.Page(
                data = response.photos!!,
                prevKey = if (curPage == 1) null else curPage.minus(1),
                nextKey = if (curPage >= response.total_results) null else curPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}