package com.example.usagi_tienda_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = true
)
abstract class UsagiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UsagiDatabase? = null

        /**
         * Configuración de la base de datos Room.
         * - Expone el `UserDao` y crea la instancia singleton.
         * - Usa `fallbackToDestructiveMigration` sólo para desarrollo (borra datos al cambiar schema).
         */
        fun getInstance(context: Context): UsagiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsagiDatabase::class.java,
                    "usagi-db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}