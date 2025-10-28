package com.example.usagi_tienda_app.presentation.auth

import com.example.usagi_tienda_app.data.FavoriteCategory

/**
 * Estados de UI para autenticación (Login y Registro).
 * - Se usan en Compose para renderizar campos, errores y progreso.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val message: String? = null
)

/**
 * `LoginUiState` mantiene email, contraseña, errores y bandera de éxito.
 */
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phone: String? = null,
    val favoriteCategory: FavoriteCategory? = null,
    val errorFullName: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val errorPhone: String? = null,
    val errorCategory: String? = null,
    val isLoading: Boolean = false,
    val successUserId: Long? = null,
    val message: String? = null
)
/**
 * `RegisterUiState` contiene los campos del formulario y errores por campo.
 * - `successUserId` indica registro exitoso; la UI navega y luego se limpia.
 */