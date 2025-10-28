package com.example.usagi_tienda_app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.usagi_tienda_app.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // Observamos el carrito para obtener la cantidad total de ítems.
    val cartItems by com.example.usagi_tienda_app.data.CartStore.items.collectAsState()
    val cartCount = com.example.usagi_tienda_app.data.CartStore.count()

    Scaffold(
        topBar = {
            // Barra superior con título y acceso rápido al carrito.
            TopAppBar(
                title = { Text("Usagi Figures", fontWeight = FontWeight.SemiBold) },
                actions = {
                    // Botón de navegación al carrito mostrando la cantidad total.
                    TextButton(onClick = { navController.navigate(Routes.CART) }) {
                        Text("Carrito ($cartCount)")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Texto de bienvenida.
            Text(
                text = "Bienvenido a la tienda",
                style = MaterialTheme.typography.titleMedium
            )
            // Breve descripción.
            Text(
                text = "Explora categorías como Chiikawa, Peluches, Llaveros y Accesorios.",
                style = MaterialTheme.typography.bodyMedium
            )
            // Botón para ir al catálogo.
            Button(onClick = { navController.navigate(Routes.CATALOG) }, enabled = true) {
                Text("Ver catálogo")
            }
            // Botón para ir al carrito mostrando la cantidad total.
            OutlinedButton(onClick = { navController.navigate(Routes.CART) }, enabled = true) {
                Text("Ver carrito ($cartCount)")
            }
        }
    }
}