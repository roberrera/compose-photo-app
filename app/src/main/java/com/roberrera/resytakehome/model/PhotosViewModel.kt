package com.roberrera.resytakehome.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roberrera.resytakehome.network.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    private var _photos = MutableStateFlow<List<Photo?>?>(emptyList())
    var photos: StateFlow<List<Photo?>?> = _photos.asStateFlow()
    private var _selectedPhotoUrl = MutableStateFlow<String?>(null)
    var selectedPhotoUrl: StateFlow<String?> = _selectedPhotoUrl.asStateFlow()
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private var _pageNumber: MutableStateFlow<Int> = MutableStateFlow(0)

    fun fetchPhotos() {
        // Guard against multiple simultaneous requests
        if (isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nextPageToFetch: Int = _pageNumber.value + 1
                val newPhotos: List<Photo?>? = repository.fetchPhotos(page = nextPageToFetch)

                if (!newPhotos.isNullOrEmpty()) {
                    val fetchedPhotos = _photos.value ?: emptyList()
                    _photos.value = fetchedPhotos + newPhotos
                    _pageNumber.value = nextPageToFetch
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPhotoById(width: Int, height: Int, id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedPhotoUrl.value = repository.fetchPhotoById(width, height, id)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedPhotoUrl() {
        _selectedPhotoUrl.value = null
    }
}
