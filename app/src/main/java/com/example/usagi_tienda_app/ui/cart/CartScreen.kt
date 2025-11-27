package com.example.usagi_tienda_app.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.usagi_tienda_app.data.CartStore
import com.example.usagi_tienda_app.data.CartItem
import com.example.usagi_tienda_app.data.CouponStore
import com.example.usagi_tienda_app.ui.components.UsagiTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    // observamos el estado del carrito para que la ui se actualice automaticamente
    val items by CartStore.items.collectAsState()

    Scaffold(
        topBar = {
            // Barra superior consistente
            UsagiTopBar(
                navController = navController,
                title = "Carrito",
                showBack = true
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // si el carrito esta vacio mostramos un mensaje y un boton para volver
            if (items.isEmpty()) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                OutlinedButton(onClick = { navController.popBackStack() }) { Text("Volver") }
            } else {
                // lista de items del carrito cada fila muestra nombre precio y controles de cantidad
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                    items(items) { item ->
                        CartItemRow(item)
                    }
                }

                // separador visual y resumen de precios
                HorizontalDivider()
                val subtotal = CartStore.total()
                val appliedCode = CouponStore.appliedCoupon
                val discountRate = CouponStore.discount // 0.0 .. 1.0
                val discount = (subtotal * discountRate).toInt()
                val total = subtotal - discount

                Card {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Subtotal: $${subtotal}", style = MaterialTheme.typography.titleMedium)
                        if (appliedCode != null) {
                            val percent = (discountRate * 100).toInt()
                            Text("Cupón: ${appliedCode} (-${percent}%)", style = MaterialTheme.typography.bodyMedium)
                            Text("Descuento: -$${discount}", style = MaterialTheme.typography.bodyMedium)
                            OutlinedButton(onClick = { CouponStore.clear() }) { Text("Quitar cupón") }
                        }
                        Text("Total: $${total}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { CartStore.clear() }) { Text("Vaciar carrito") }
                    Button(onClick = { navController.navigate(com.example.usagi_tienda_app.Routes.CHECKOUT) }, enabled = items.isNotEmpty()) { Text("Comprar") }
                    Button(onClick = { navController.navigate(com.example.usagi_tienda_app.Routes.COUPON_SCAN) }) { Text("Escanear cupón") }
                }
                OutlinedButton(onClick = { navController.popBackStack() }) { Text("Volver") }
            }
        }
    }
}

@Composable
private fun CartItemRow(item: CartItem) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // datos principales del item
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.figure.name, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${item.figure.price}", style = MaterialTheme.typography.bodyMedium)
                Text("Cantidad: ${item.quantity}", style = MaterialTheme.typography.bodySmall)
                Text("Subtotal: $${item.figure.price * item.quantity}", style = MaterialTheme.typography.bodySmall)
            }
            // controles para modificar la cantidad o eliminar el item
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                // deshabilitamos el boton menos cuando la cantidad es 1 para evitar borrar por accidente
                OutlinedButton(onClick = { CartStore.decrement(item.figure.id) }, enabled = item.quantity > 1) { Text("-") }
                OutlinedButton(onClick = { CartStore.increment(item.figure.id) }) { Text("+") }
                OutlinedButton(onClick = { CartStore.remove(item.figure.id) }) {
                    Text("Eliminar")
                }
            }
        }
    }
}
