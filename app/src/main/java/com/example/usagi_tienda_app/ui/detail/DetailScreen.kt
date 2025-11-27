package com.example.usagi_tienda_app.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.usagi_tienda_app.data.MockCatalog
import com.example.usagi_tienda_app.data.Figure
import com.example.usagi_tienda_app.Routes
import kotlinx.coroutines.launch
import android.util.Log
import com.example.usagi_tienda_app.ui.components.UsagiTopBar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, figureId: Long) {
    // obtenemos la figura segun el id si no existe mostramos un mensaje
    // estado para mostrar mensajes snackbar cuando agregamos al carrito
    // barra superior con titulo y acceso rapido al carrito
    // contador del carrito en el top bar
    // si la figura no existe mostramos un mensaje y un boton para volver
    // informacion basica de la figura
    // boton agregar al carrito solo habilitado si la figura esta disponible
    // mostramos confirmacion
    // boton para volver a la pantalla anterior
    
    val figure: Figure? = try {
        if (figureId <= 0) {
            Log.w("DetailScreen", "id invalido: $figureId")
            null
        } else {
            MockCatalog.getById(figureId)
        }
    } catch (e: Exception) {
        Log.e("DetailScreen", "error obteniendo figura con id $figureId", e)
        null
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            // Barra superior consistente
            UsagiTopBar(
                navController = navController,
                title = figure?.name ?: "Detalle",
                showBack = true,
                showCartAction = true
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (figure == null) {
                // Si la figura no existe, mostramos un mensaje y un botón para volver.
                Text("Figura no encontrada", style = MaterialTheme.typography.titleMedium)
                Text("El producto que buscas no existe o ha sido eliminado.", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { 
                        try {
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Log.e("DetailScreen", "error volviendo atras", e)
                            // fallback: navegar al catalogo
                            try {
                                navController.navigate(Routes.CATALOG) {
                                    popUpTo(Routes.CATALOG) { inclusive = true }
                                }
                            } catch (e2: Exception) {
                                Log.e("DetailScreen", "error navegando al catalogo fallback", e2)
                            }
                        }
                    }
                ) { 
                    Text("Volver") 
                }
            } else {
                    // Información básica de la figura.
                    Text(
                        text = figure.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Precio: $${String.format(Locale.getDefault(), "%.2f", figure.price.toDouble())}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "Categoría: ${figure.category}", 
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    if (!figure.available) {
                        Text(
                            text = "⚠️ Producto no disponible",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Botón "Agregar al carrito". Solo habilitado si la figura está disponible.
                    Button(
                        onClick = {
                            try {
                                if (figure.available && figure.price > 0) {
                                    com.example.usagi_tienda_app.data.CartStore.add(figure)
                                    // Mostramos confirmación.
                                    scope.launch {
                                        try {
                                            snackbarHostState.showSnackbar("${figure.name} agregado al carrito")
                                        } catch (e: Exception) {
                                            Log.e("DetailScreen", "error mostrando snackbar", e)
                                        }
                                    }
                                } else {
                                    scope.launch {
                                        try {
                                            snackbarHostState.showSnackbar("No se puede agregar este producto")
                                        } catch (e: Exception) {
                                            Log.e("DetailScreen", "error mostrando snackbar error", e)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("DetailScreen", "error agregando al carrito", e)
                                scope.launch {
                                    try {
                                        snackbarHostState.showSnackbar("Error al agregar al carrito")
                                    } catch (e2: Exception) {
                                        Log.e("DetailScreen", "error mostrando snackbar error fallback", e2)
                                    }
                                }
                            }
                        },
                        enabled = figure.available && figure.price > 0,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (figure.available) "Agregar al carrito" else "No disponible")
                    }
                    
                    // Botón para volver a la pantalla anterior.
                    OutlinedButton(
                        onClick = { 
                            try {
                                navController.popBackStack()
                            } catch (e: Exception) {
                                Log.e("DetailScreen", "error volviendo atras", e)
                                // fallback: navegar al catalogo
                                try {
                                    navController.navigate(Routes.CATALOG) {
                                        popUpTo(Routes.CATALOG) { inclusive = true }
                                    }
                                } catch (e2: Exception) {
                                    Log.e("DetailScreen", "error navegando al catalogo fallback", e2)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver al catálogo")
                    }
            }
        }
    }
}
