package com.example.usagi_tienda_app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
/**
 * Entidad de base de datos que representa un usuario registrado.
 * - Se almacena en la tabla `users` mediante Room.
 * - Incluye datos básicos de perfil y preferencias.
 */
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val phone: String?,
    val favoriteCategory: FavoriteCategory
)

enum class FavoriteCategory {
    CHIIKAWA,
    PELUCHES,
    LLAVEROS,
    ACCESORIOS
}

/**
 * Campos principales:
 * - `id`: Identificador autogenerado.
 * - `fullName`: Nombre completo del usuario.
 * - `email`: Correo (debe ser único).
 * - `passwordHash`: Hash de la contraseña (no se guarda en texto plano).
 * - `phone`: Teléfono opcional.
 * - `favoriteCategory`: Categoría favorita para recomendaciones.
 */