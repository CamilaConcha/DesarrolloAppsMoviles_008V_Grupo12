package com.example.usagi_tienda_app.ui.coupon

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponScannerScreen(navController: NavController) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        val msg = if (granted) "Permiso de cámara concedido" else "Permiso de cámara denegado"
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        try {
            if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
        } catch (e: Exception) {
            Log.e("CouponScannerScreen", "error solicitando permiso inicial", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear cupón") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (!hasPermission) {
                // Vista para solicitar permiso de cámara
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "📷", style = MaterialTheme.typography.displayLarge)
                    Text(text = "Necesitamos permiso para usar la cámara", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Para escanear cupones necesitamos acceso a tu cámara", style = MaterialTheme.typography.bodyMedium)

                    Button(onClick = {
                        try {
                            launcher.launch(Manifest.permission.CAMERA)
                        } catch (e: Exception) {
                            Log.e("CouponScannerScreen", "error lanzando solicitud de permiso", e)
                            Toast.makeText(context, "Error al solicitar permiso", Toast.LENGTH_SHORT).show()
                        }
                    }) { Text("Conceder permiso") }

                    OutlinedButton(onClick = {
                        try {
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Log.e("CouponScannerScreen", "error volviendo sin permiso", e)
                        }
                    }) { Text("Volver al carrito") }
                }
            } else {
                // Permiso concedido: simulación de escaneo de cupones
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "✅", style = MaterialTheme.typography.displayLarge)
                    Text(text = "Cámara lista para escanear", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Funcionalidad de escaneo en desarrollo", style = MaterialTheme.typography.bodyMedium)

                    Card(modifier = Modifier.fillMaxWidth(0.8f)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Cupones de prueba:", style = MaterialTheme.typography.titleSmall)

                            Button(
                                onClick = {
                                    try {
                                        val success = com.example.usagi_tienda_app.data.CouponStore.apply("USAGI10")
                                        if (success) {
                                            Toast.makeText(context, "Cupón USAGI10 aplicado (10% descuento)", Toast.LENGTH_LONG).show()
                                            navController.popBackStack()
                                        } else {
                                            Toast.makeText(context, "Error aplicando cupón", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Log.e("CouponScannerScreen", "error aplicando cupon USAGI10", e)
                                        Toast.makeText(context, "Error al aplicar cupón", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Simular: USAGI10 (10% desc.)") }

                            Button(
                                onClick = {
                                    try {
                                        val success = com.example.usagi_tienda_app.data.CouponStore.apply("CHIIKAWA5")
                                        if (success) {
                                            Toast.makeText(context, "Cupón CHIIKAWA5 aplicado (5% descuento)", Toast.LENGTH_LONG).show()
                                            navController.popBackStack()
                                        } else {
                                            Toast.makeText(context, "Error aplicando cupón", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Log.e("CouponScannerScreen", "error aplicando cupon CHIIKAWA5", e)
                                        Toast.makeText(context, "Error al aplicar cupón", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Simular: CHIIKAWA5 (5% desc.)") }
                        }
                    }

                    OutlinedButton(onClick = {
                        try {
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Log.e("CouponScannerScreen", "error volviendo al carrito", e)
                        }
                    }) { Text("Volver al carrito") }
                }
            }
        }
    }
}