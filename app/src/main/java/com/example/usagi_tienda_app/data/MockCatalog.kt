package com.example.usagi_tienda_app.data

import android.util.Log

data class Figure(
    val id: Long,
    val name: String,
    val price: Int,
    val category: FavoriteCategory,
    val description: String,
    val available: Boolean,
    val imageUrl: String? = null,
)

/**
 * catalogo de productos de ejemplo mock usado por la app
 * actualizado con items inspirados en chiikawa y merch afin
 */
object MockCatalog {
    val figures: List<Figure> = listOf(
        Figure(1, "Chiikawa Plush - Hachiware 20cm", 18990, FavoriteCategory.PELUCHES, "Peluche suave de Hachiware 20cm, licencia alternativa.", true, null),
        Figure(2, "Chiikawa Plush - Usagi 20cm", 18990, FavoriteCategory.PELUCHES, "Peluche de Usagi 20cm, ideal para regalo.", true, null),
        Figure(3, "Chiikawa Keychain - Set 3 personajes", 12990, FavoriteCategory.LLAVEROS, "Set de llaveros de Chiikawa: Hachiware, Usagi y Chiikawa.", true, null),
        Figure(4, "Chiikawa Tote Bag", 15990, FavoriteCategory.ACCESORIOS, "Bolsa tote con estampado Chiikawa, resistente y liviana.", true, null),
        Figure(5, "Chiikawa Stationery - Stickers Pack", 6990, FavoriteCategory.ACCESORIOS, "Pack de stickers Chiikawa para cuadernos y decoración.", true, null),
        Figure(6, "Chiikawa Mug - 350ml", 9990, FavoriteCategory.ACCESORIOS, "Taza de cerámica 350ml con diseño Chiikawa.", false, null),
        Figure(7, "Chiikawa Mini Figure - Hachiware", 14990, FavoriteCategory.CHIIKAWA, "Mini figura de Hachiware para escritorio.", true, null),
        Figure(8, "Chiikawa Mini Figure - Usagi", 14990, FavoriteCategory.CHIIKAWA, "Mini figura de Usagi, edición limitada.", true, null),
    )

    // getbyid busca una figura por id con validacion y manejo de errores
    fun getById(id: Long): Figure? {
        return try {
            if (id <= 0) {
                Log.w("MockCatalog", "id invalido: $id")
                return null
            }
            
            val figure = figures.find { it.id == id }
            if (figure == null) {
                Log.w("MockCatalog", "figura no encontrada con id: $id")
            } else {
                Log.d("MockCatalog", "figura encontrada: ${figure.name} (id: $id)")
            }
            
            figure
        } catch (e: Exception) {
            Log.e("MockCatalog", "error buscando figura con id: $id", e)
            null
        }
    }
    
    // getall devuelve todas las figuras con manejo de errores
    fun getAll(): List<Figure> {
        return try {
            Log.d("MockCatalog", "obteniendo todas las figuras: ${figures.size} items")
            figures
        } catch (e: Exception) {
            Log.e("MockCatalog", "error obteniendo todas las figuras", e)
            emptyList()
        }
    }
    
    // getbycategory filtra figuras por categoria con validacion
    fun getByCategory(category: FavoriteCategory?): List<Figure> {
        return try {
            if (category == null) {
                Log.d("MockCatalog", "categoria null, devolviendo todas las figuras")
                return getAll()
            }
            
            val filtered = figures.filter { it.category == category }
            Log.d("MockCatalog", "figuras filtradas por categoria $category: ${filtered.size} items")
            filtered
        } catch (e: Exception) {
            Log.e("MockCatalog", "error filtrando por categoria: $category", e)
            emptyList()
        }
    }
    
    // getavailable devuelve solo figuras disponibles
    fun getAvailable(): List<Figure> {
        return try {
            val available = figures.filter { it.available }
            Log.d("MockCatalog", "figuras disponibles: ${available.size} de ${figures.size}")
            available
        } catch (e: Exception) {
            Log.e("MockCatalog", "error obteniendo figuras disponibles", e)
            emptyList()
        }
    }
}