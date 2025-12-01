package com.roberrera.resytakehome.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roberrera.resytakehome.network.NoConnectivityException
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

    private val _photos = MutableStateFlow<List<Photo?>?>(emptyList())
    val photos: StateFlow<List<Photo?>?> = _photos.asStateFlow()
    private val _selectedPhotoUrl = MutableStateFlow<String?>(null)
    val selectedPhotoUrl: StateFlow<String?> = _selectedPhotoUrl.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchPhotos() {
        viewModelScope.launch {
            if (_photos.value.isNullOrEmpty()) {
                _isLoading.value = true
                try {
                    _photos.value = repository.fetchPhotos()
                } catch (e: NoConnectivityException) {
                    _error.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun fetchPhotoById(width: Int, height: Int, id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedPhotoUrl.value = repository.fetchPhotoById(width, height, id)
            } catch (e: NoConnectivityException) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedPhotoUrl() {
        _selectedPhotoUrl.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
