package com.example.usagi_tienda_app.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log

// CartItem representa un producto en el carrito y cuántas unidades tiene.
// "figure" es el producto y "quantity" cuántas unidades del mismo.
data class CartItem(
    val figure: Figure,
    val quantity: Int,
)

// CartStore es un objeto (singleton) que mantiene el estado del carrito en memoria.
// Usa StateFlow para que la UI (Compose) se actualice automáticamente cuando cambie el carrito.
object CartStore {
    // _items es el estado interno (privado). Empezamos con una lista vacía.
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    // items expone el estado de solo lectura para que la UI se suscriba.
    val items: StateFlow<List<CartItem>> = _items

    // add: agrega una unidad de la figura al carrito.
    // Si la figura ya existe, incrementa su cantidad; si no, la agrega con cantidad 1.
    fun add(figure: Figure) {
        try {
            if (figure.price < 0) {
                Log.w("CartStore", "precio invalido para figura ${figure.id}: ${figure.price}")
                return
            }
            val existing = _items.value.find { it.figure.id == figure.id }
            val updated = if (existing == null) {
                _items.value + CartItem(figure, 1)
            } else {
                _items.value.map { if (it.figure.id == figure.id) it.copy(quantity = it.quantity + 1) else it }
            }
            _items.value = updated
            Log.d("CartStore", "figura agregada: ${figure.name}, total items: ${count()}")
        } catch (e: Exception) {
            Log.e("CartStore", "error agregando figura al carrito", e)
        }
    }

    // increment: suma 1 a la cantidad del ítem con el id indicado.
    fun increment(id: Long) {
        try {
            if (id <= 0) {
                Log.w("CartStore", "id invalido para incrementar: $id")
                return
            }
            val item = _items.value.find { it.figure.id == id }
            if (item == null) {
                Log.w("CartStore", "item no encontrado para incrementar: $id")
                return
            }
            if (item.quantity >= 99) {
                Log.w("CartStore", "cantidad maxima alcanzada para item: $id")
                return
            }
            _items.value = _items.value.map { if (it.figure.id == id) it.copy(quantity = it.quantity + 1) else it }
            Log.d("CartStore", "cantidad incrementada para item $id")
        } catch (e: Exception) {
            Log.e("CartStore", "error incrementando cantidad", e)
        }
    }

    // decrement: resta 1 a la cantidad del ítem con el id indicado.
    // Si la cantidad queda en 0, se elimina el ítem del carrito.
    fun decrement(id: Long) {
        try {
            if (id <= 0) {
                Log.w("CartStore", "id invalido para decrementar: $id")
                return
            }
            val item = _items.value.find { it.figure.id == id }
            if (item == null) {
                Log.w("CartStore", "item no encontrado para decrementar: $id")
                return
            }
            _items.value = _items.value
                .map { if (it.figure.id == id) it.copy(quantity = it.quantity - 1) else it }
                .filter { it.quantity > 0 }
            Log.d("CartStore", "cantidad decrementada para item $id")
        } catch (e: Exception) {
            Log.e("CartStore", "error decrementando cantidad", e)
        }
    }

    // remove: elimina por completo el ítem (sin importar su cantidad).
    fun remove(id: Long) {
        try {
            if (id <= 0) {
                Log.w("CartStore", "id invalido para remover: $id")
                return
            }
            val sizeBefore = _items.value.size
            _items.value = _items.value.filter { it.figure.id != id }
            val sizeAfter = _items.value.size
            if (sizeBefore == sizeAfter) {
                Log.w("CartStore", "item no encontrado para remover: $id")
            } else {
                Log.d("CartStore", "item removido: $id")
            }
        } catch (e: Exception) {
            Log.e("CartStore", "error removiendo item", e)
        }
    }

    // clear: vacía el carrito.
    fun clear() {
        try {
            val itemCount = _items.value.size
            _items.value = emptyList()
            Log.d("CartStore", "carrito vaciado, $itemCount items removidos")
        } catch (e: Exception) {
            Log.e("CartStore", "error vaciando carrito", e)
        }
    }

    // total: devuelve el precio total (suma de subtotales: precio * cantidad por ítem).
    fun total(): Int {
        return try {
            val total = _items.value.sumOf { 
                if (it.figure.price < 0 || it.quantity < 0) {
                    Log.w("CartStore", "valores invalidos en item ${it.figure.id}: precio=${it.figure.price}, cantidad=${it.quantity}")
                    0
                } else {
                    it.figure.price * it.quantity
                }
            }
            total
        } catch (e: Exception) {
            Log.e("CartStore", "error calculando total", e)
            0
        }
    }

    // count: devuelve la cantidad total de unidades en el carrito (suma de cantidades).
    fun count(): Int {
        return try {
            val count = _items.value.sumOf { 
                if (it.quantity < 0) {
                    Log.w("CartStore", "cantidad invalida en item ${it.figure.id}: ${it.quantity}")
                    0
                } else {
                    it.quantity
                }
            }
            count
        } catch (e: Exception) {
            Log.e("CartStore", "error calculando cantidad total", e)
            0
        }
    }
}