package com.example.usagi_tienda_app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.usagi_tienda_app.Routes
import com.example.usagi_tienda_app.ui.components.UsagiTopBar
import androidx.compose.ui.res.stringResource
import com.example.usagi_tienda_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // Observamos el carrito para obtener la cantidad total de ítems.
    val cartItems by com.example.usagi_tienda_app.data.CartStore.items.collectAsState()
    val cartCount = com.example.usagi_tienda_app.data.CartStore.count()
    val snackbarHostState = remember { SnackbarHostState() }
    val purchaseMsg by com.example.usagi_tienda_app.data.PurchaseEvents.message.collectAsState()

    Scaffold(
        topBar = {
            // Barra superior consistente
            UsagiTopBar(
                navController = navController,
                title = stringResource(R.string.title_home),
                showBack = true,
                showCartAction = true
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                text = stringResource(R.string.home_welcome),
                style = MaterialTheme.typography.titleMedium
            )
            // Breve descripción.
            Text(
                text = stringResource(R.string.home_desc),
                style = MaterialTheme.typography.bodyMedium
            )
            // Botón para ir al catálogo.
            Button(onClick = { navController.navigate(Routes.CATALOG) }, enabled = true) {
                Text(stringResource(R.string.home_view_catalog))
            }
            // Botón para ver fotos remotas (prueba offline con imágenes).
            Button(onClick = { navController.navigate(Routes.PHOTOS) }, enabled = true) {
                Text(stringResource(R.string.home_view_photos))
            }
            // Botón para ir al carrito mostrando la cantidad total.
            OutlinedButton(onClick = { navController.navigate(Routes.CART) }, enabled = true) {
                Text(stringResource(R.string.home_view_cart, cartCount))
            }
        }
    }

    // Mostrar Snackbar cuando haya un mensaje de compra realizada
    LaunchedEffect(purchaseMsg) {
        val msg = purchaseMsg
        if (msg != null) {
            snackbarHostState.showSnackbar(message = msg, withDismissAction = true)
            com.example.usagi_tienda_app.data.PurchaseEvents.clear()
        }
    }
}
