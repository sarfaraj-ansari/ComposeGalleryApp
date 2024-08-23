package com.example.galleryappcompose.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.galleryappcompose.data.models.PhotoResponse
import com.example.galleryappcompose.data.network.NetworkResult
import com.example.galleryappcompose.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MViewmodel @Inject constructor(private val repository: Repository) : ViewModel() {

    val allPhotos = repository.getAllPhotos().cachedIn(viewModelScope)

    fun getSearchedPhotos(query: String) = repository.getSearchedPhotos(query = query).cachedIn(viewModelScope)

    val allVideos = repository.getPopularVideos().cachedIn(viewModelScope)

    fun getSearchedVideos(query: String) = repository.getSearchedVideos(query = query).cachedIn(viewModelScope)

    private val _imageDetails = MutableLiveData<NetworkResult<PhotoResponse>>()
    var imageDetails: LiveData<NetworkResult<PhotoResponse>> = _imageDetails
    init {
        viewModelScope.launch {
            _imageDetails.postValue(NetworkResult.Loading)
            _imageDetails.postValue(repository.getImageDetails())
        }
    }

}