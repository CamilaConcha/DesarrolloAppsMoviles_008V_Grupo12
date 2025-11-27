package com.example.usagi_tienda_app.data.remote

import com.example.usagi_tienda_app.data.model.Photo
import retrofit2.http.GET

/**
 * Servicio Retrofit que uso para consumir recursos públicos de JSONPlaceholder.
 */
interface ApiService {
    /** Obtiene la lista de fotos (contiene URLs de imágenes). */
    @GET("photos")
    suspend fun getPhotos(): List<Photo>
}
