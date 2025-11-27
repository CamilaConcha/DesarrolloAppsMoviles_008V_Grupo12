package com.example.usagi_tienda_app.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Store sencillo para emitir un mensaje de "compra realizada"
object PurchaseEvents {
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun emit(msg: String) {
        _message.value = msg
    }

    fun clear() {
        _message.value = null
    }
}

