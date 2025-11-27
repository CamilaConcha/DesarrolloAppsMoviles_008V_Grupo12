package com.example.usagi_tienda_app.data

import com.example.usagi_tienda_app.data.model.Photo
import com.example.usagi_tienda_app.data.remote.RetrofitInstance

/**
 * Repositorio para acceder a fotos remotas.
 */
class PhotoRepository {
    suspend fun getPhotos(): List<Photo> = RetrofitInstance.api.getPhotos()
}