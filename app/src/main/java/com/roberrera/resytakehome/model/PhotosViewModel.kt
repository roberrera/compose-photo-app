package com.roberrera.resytakehome.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.roberrera.resytakehome.network.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

 @HiltViewModel
class PhotosViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PhotoRepository
) : ViewModel() {

     var photos: StateFlow<List<Photo?>?> = _photos.asStateFlow()
     private var _photos = MutableStateFlow<List<Photo?>?>(emptyList())
    private val photoRepository = PhotoRepository()

    suspend fun fetchPhotos() {
        val fetchedPhotos = photoRepository.fetchPhotos()
        _photos.value = fetchedPhotos

    }
}