package com.example.usagi_tienda_app.data.model

/**
 * Modelo de datos que uso para el recurso `/photos` de JSONPlaceholder.
 * Incluye URL de imagen y miniatura para pruebas con Coil.
 */
data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
