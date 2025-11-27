package com.example.usagi_tienda_app.domain.usecase

import com.example.usagi_tienda_app.data.LoginResult
import com.example.usagi_tienda_app.data.UserRepository

class LoginUser(private val repository: UserRepository) {
    suspend fun execute(email: String, password: String): LoginResult {
        val e = email.trim()
        val p = password
        if (e.isEmpty() || p.isEmpty()) {
            return LoginResult.EmailNotFound
        }
        return repository.login(e, p)
    }
}

/**
 * Caso de uso: intentar iniciar sesión.
 * - Recibe email y contraseña.
 * - Consulta al repositorio y retorna `LoginResult`.
 */