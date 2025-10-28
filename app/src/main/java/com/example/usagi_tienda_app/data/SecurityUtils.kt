package com.example.usagi_tienda_app.data

import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64

object SecurityUtils {
    private const val SALT_BYTES = 16

    /**
     * Utilidades de seguridad básicas para hashing de contraseñas.
     * - `generateSalt()`: crea un salt aleatorio.
     * - `hashPassword(password, salt)`: retorna el hash seguro para comparar/almacenar.
     * Nota: en producción se recomienda usar algoritmos de hashing lentos (p.ej. PBKDF2, bcrypt).
     */
    fun generateSalt(): String {
        val bytes = ByteArray(SALT_BYTES)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun hashPassword(password: String, saltBase64: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val salt = Base64.decode(saltBase64, Base64.NO_WRAP)
        digest.update(salt)
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }
}