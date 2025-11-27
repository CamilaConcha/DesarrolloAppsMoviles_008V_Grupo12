package com.example.usagi_tienda_app.presentation.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usagi_tienda_app.data.PhotoRepository
import com.example.usagi_tienda_app.data.model.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PhotosUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val photos: List<Photo> = emptyList()
)

class PhotosViewModel(
    private val repository: PhotoRepository = PhotoRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(PhotosUiState())
    val state: StateFlow<PhotosUiState> = _state

    fun load() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val data = repository.getPhotos().take(50) // limitar para UI
                _state.value = PhotosUiState(isLoading = false, photos = data)
            } catch (e: Exception) {
                _state.value = PhotosUiState(isLoading = false, error = e.message)
            }
        }
    }
}