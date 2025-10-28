package com.example.usagi_tienda_app.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.usagi_tienda_app.data.UsagiDatabase
import com.example.usagi_tienda_app.data.UserRepository

/**
 * Factory para crear `AuthViewModel` con dependencias.
 * - Obtiene la instancia de `UsagiDatabase` y construye `UserRepository`.
 * - Facilita inyección de dependencias en Compose.
 */
class AuthViewModelFactory(private val appContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = UsagiDatabase.getInstance(appContext)
        val repo = UserRepository(db.userDao())
        return AuthViewModel(repo) as T
    }
}