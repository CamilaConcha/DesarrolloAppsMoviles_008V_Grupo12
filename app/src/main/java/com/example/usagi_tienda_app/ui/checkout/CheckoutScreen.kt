package com.example.usagi_tienda_app.ui.checkout

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.usagi_tienda_app.Routes
import com.example.usagi_tienda_app.data.CartStore
import com.example.usagi_tienda_app.data.CouponStore
import com.example.usagi_tienda_app.ui.components.UsagiTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController) {
    val items by CartStore.items.collectAsState()

    // Si no hay items, regresamos al carrito
    LaunchedEffect(items) {
        try {
            if (items.isEmpty()) {
                navController.popBackStack()
            }
        } catch (e: Exception) {
            Log.e("CheckoutScreen", "error volviendo por carrito vacio", e)
        }
    }

    var showConfirm by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf("Tarjeta") }

    Scaffold(
        topBar = {
            UsagiTopBar(
                navController = navController,
                title = "Checkout",
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
            // Resumen de compra
            val subtotal = CartStore.total()
            val discountRate = CouponStore.discount // 0.0 .. 1.0
            val discount = (subtotal * discountRate).toInt()
            val total = subtotal - discount

            Card {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Resumen", style = MaterialTheme.typography.titleMedium)
                    Text("Subtotal: $${subtotal}", style = MaterialTheme.typography.bodyMedium)
                    val appliedCode = CouponStore.appliedCoupon
                    if (appliedCode != null && discountRate > 0.0) {
                        val percent = (discountRate * 100).toInt()
                        Text("Cupón aplicado: ${appliedCode} (-${percent}%)", style = MaterialTheme.typography.bodyMedium)
                        Text("Descuento: -$${discount}", style = MaterialTheme.typography.bodyMedium)
                        OutlinedButton(onClick = { CouponStore.clear() }) { Text("Quitar cupón") }
                    } else {
                        Text("Sin cupón aplicado", style = MaterialTheme.typography.bodyMedium)
                        OutlinedButton(onClick = { navController.navigate(Routes.COUPON_SCAN) }) { Text("Escanear cupón") }
                    }
                    Text("Total a pagar: $${total}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }

            // Método de pago simulado para demostración
            Text("Método de pago", style = MaterialTheme.typography.titleMedium)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PaymentOptionRow(
                    label = "Tarjeta",
                    selected = paymentMethod == "Tarjeta",
                    onSelect = { paymentMethod = "Tarjeta" }
                )
                PaymentOptionRow(
                    label = "Efectivo",
                    selected = paymentMethod == "Efectivo",
                    onSelect = { paymentMethod = "Efectivo" }
                )
                PaymentOptionRow(
                    label = "Transferencia",
                    selected = paymentMethod == "Transferencia",
                    onSelect = { paymentMethod = "Transferencia" }
                )
            }

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { navController.popBackStack() }) { Text("Volver al carrito") }
                Button(onClick = { showConfirm = true }, enabled = items.isNotEmpty()) { Text("Pagar ahora (ficticio)") }
            }

            if (showConfirm) {
                AlertDialog(
                    onDismissRequest = { showConfirm = false },
                    confirmButton = {
                        Button(onClick = {
                            try {
                                // Limpiamos estados y navegamos al Home
                                CartStore.clear()
                                CouponStore.clear()
                                com.example.usagi_tienda_app.data.PurchaseEvents.emit("Compra realizada con éxito")
                                showConfirm = false
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.CART) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                Log.e("CheckoutScreen", "error confirmando pago", e)
                                showConfirm = false
                            }
                        }) { Text("Confirmar pago") }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showConfirm = false }) { Text("Cancelar") }
                    },
                    title = { Text("Pago realizado") },
                    text = { Text("Tu pago ficticio por $${total} con ${paymentMethod} fue exitoso.") }
                )
            }
        }
    }
}

@Composable
private fun PaymentOptionRow(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        RadioButton(selected = selected, onClick = onSelect)
    }
}
