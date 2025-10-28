package com.example.usagi_tienda_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO de Room para acceder a usuarios.
 * - Define consultas para buscar por email y registrar un usuario.
 * - Room implementa automáticamente los métodos en tiempo de compilación.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
}