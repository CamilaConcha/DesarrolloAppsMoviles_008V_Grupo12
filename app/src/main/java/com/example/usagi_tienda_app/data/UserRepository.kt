package com.example.usagi_tienda_app.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class RegisterResult {
    data class Success(val userId: Long) : RegisterResult()
    object EmailAlreadyExists : RegisterResult()
    data class UnknownError(val message: String) : RegisterResult()
}

sealed class LoginResult {
    object Success : LoginResult()
    object EmailNotFound : LoginResult()
    object IncorrectPassword : LoginResult()
}

/**
 * Repositorio que encapsula acceso a datos de usuarios.
 * - Expone operaciones de alto nivel: login y registro.
 * - Maneja hashing de contraseñas y validaciones básicas.
 */
class UserRepository(private val userDao: UserDao) {
    suspend fun isEmailTaken(email: String): Boolean = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email) != null
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        phone: String?,
        favoriteCategory: FavoriteCategory
    ): RegisterResult = withContext(Dispatchers.IO) {
        try {
            val salt = SecurityUtils.generateSalt()
            val hash = SecurityUtils.hashPassword(password, salt)
            val id = userDao.insertUser(
                UserEntity(
                    fullName = fullName,
                    email = email,
                    passwordHash = hash,
                    salt = salt,
                    phone = phone,
                    favoriteCategory = favoriteCategory
                )
            )
            RegisterResult.Success(id)
        } catch (e: android.database.sqlite.SQLiteConstraintException) {
            RegisterResult.EmailAlreadyExists
        } catch (e: Exception) {
            RegisterResult.UnknownError(e.message ?: "Error desconocido")
        }
    }

    suspend fun login(email: String, password: String): LoginResult = withContext(Dispatchers.IO) {
        val user = userDao.getUserByEmail(email) ?: return@withContext LoginResult.EmailNotFound
        val hash = SecurityUtils.hashPassword(password, user.salt)
        if (hash == user.passwordHash) LoginResult.Success else LoginResult.IncorrectPassword
    }
}