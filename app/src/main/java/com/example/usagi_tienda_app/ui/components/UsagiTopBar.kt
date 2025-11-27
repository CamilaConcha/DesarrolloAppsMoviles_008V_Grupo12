package com.example.usagi_tienda_app.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.usagi_tienda_app.Routes
import androidx.compose.ui.res.stringResource
import com.example.usagi_tienda_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsagiTopBar(
    navController: NavController,
    title: String,
    showBack: Boolean = false,
    showCartAction: Boolean = false,
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            if (showBack) {
                TextButton(onClick = { navController.popBackStack() }) { Text(stringResource(R.string.action_back)) }
            }
        },
        actions = {
            if (showCartAction) {
                val cartItemsState by com.example.usagi_tienda_app.data.CartStore.items.collectAsState()
                val cartCount = com.example.usagi_tienda_app.data.CartStore.count()
                TextButton(onClick = { navController.navigate(Routes.CART) }) {
                    Text(stringResource(R.string.action_cart, cartCount))
                }
            }
        }
    )
}
