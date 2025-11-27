package com.example.usagi_tienda_app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.usagi_tienda_app.theme.UsagiTheme
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.usagi_tienda_app.ui.auth.LoginScreen
import com.example.usagi_tienda_app.ui.auth.RegisterScreen
import com.example.usagi_tienda_app.ui.home.HomeScreen
import com.example.usagi_tienda_app.ui.catalog.CatalogScreen
import com.example.usagi_tienda_app.ui.detail.DetailScreen
import com.example.usagi_tienda_app.ui.cart.CartScreen
import com.example.usagi_tienda_app.ui.coupon.CouponScannerScreen
import com.example.usagi_tienda_app.ui.photos.PhotoScreen
import android.util.Log

// este objeto agrupa las rutas de la app como constantes
// se usan para navegar entre pantallas de forma segura y consistente
object Routes {
    // rutas pantallas de la app para navegar entre ellas
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CATALOG = "catalog"
    const val DETAIL = "detail"
    const val CART = "cart"
    const val COUPON_SCAN = "coupon_scan"
    const val PHOTOS = "photos"
    const val CHECKOUT = "checkout"
}

/* Componente raíz de la app: defino navegación y pantallas.
   Configuro NavHost y rutas (login, registro, catálogo, detalle, carrito, etc.).
   Orquesto estados y acciones según cada pantalla. */
@Composable
fun UsagiApp() {
    UsagiTheme {
        // creamos el controlador de navegacion
        val navController = rememberNavController()
        // navhost conecta el navcontroller con las pantallas rutas de la app
        // startdestination indica la primera pantalla al iniciar login
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN
        ) {
            // pantalla de login con manejo de errores
            composable(Routes.LOGIN) {
                LoginScreen(navController = navController)
            }
            // pantalla de registro con manejo de errores
            composable(Routes.REGISTER) {
                RegisterScreen(navController = navController)
            }
            // pantalla de inicio con manejo de errores
            composable(Routes.HOME) {
                HomeScreen(navController = navController)
            }
            // pantalla de catalogo con manejo de errores
            composable(Routes.CATALOG) {
                CatalogScreen(navController = navController)
            }
            // Pantalla de detalle: recibe un parámetro 'id' (Long) en la ruta.
            // Ejemplo de ruta: detail/{id}, donde 'id' corresponde a la figura.
            composable("${Routes.DETAIL}/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id")
                if (id == null || id <= 0) {
                    navController.navigate(Routes.CATALOG) {
                        popUpTo("${Routes.DETAIL}/{id}") { inclusive = true }
                    }
                    return@composable
                }
                DetailScreen(navController = navController, figureId = id)
            }
            // pantalla de carrito con manejo de errores
            composable(Routes.CART) {
                CartScreen(navController = navController)
            }
            // Pantalla de checkout con flujo de pago simulado y cupones
            composable(Routes.CHECKOUT) {
                com.example.usagi_tienda_app.ui.checkout.CheckoutScreen(navController = navController)
            }
            // pantalla de escaneo de cupones con manejo de errores
            composable(Routes.COUPON_SCAN) {
                CouponScannerScreen(navController = navController)
            }
            // Pantalla de fotos remotas usando JSONPlaceholder (/photos)
            composable(Routes.PHOTOS) {
                PhotoScreen(navController = navController)
            }
        }
    }
}
