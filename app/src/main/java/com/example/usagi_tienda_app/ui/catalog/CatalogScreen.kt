package com.example.usagi_tienda_app.ui.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.usagi_tienda_app.theme.UsagiTheme
import com.example.usagi_tienda_app.Routes
import com.example.usagi_tienda_app.data.FavoriteCategory
import com.example.usagi_tienda_app.data.Figure
import com.example.usagi_tienda_app.data.MockCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavController) {
    // estado de la categoria seleccionada filtro null todas las categorias
    // filtramos los productos segun la categoria seleccionada
    // observamos el carrito y mostramos la cantidad total de unidades
    // barra superior con titulo y acceso rapido al carrito
    // explora los productos por categoria
    // fila de filtros de categoria
    // lista de productos filtrados al hacer click vamos al detalle
    // filtros simples todos chiikawa peluches llaveros accesorios
    // chip basico cambia color segun seleccion
    // tarjeta simple muestra nombre precio y categoria
    // implementacion simple de flowrow usando row con espaciado no es un flowrow real
    var selectedCategory by remember { mutableStateOf<FavoriteCategory?>(null) }

    // Filtramos los productos según la categoría seleccionada.
    val filtered = remember(selectedCategory) {
        val source = MockCatalog.figures
        if (selectedCategory == null) source else source.filter { it.category == selectedCategory }
    }

    // Observamos el carrito y mostramos la cantidad total de unidades.
    val cartItems by com.example.usagi_tienda_app.data.CartStore.items.collectAsState()
    val cartCount = com.example.usagi_tienda_app.data.CartStore.count()

    Scaffold(
        topBar = {
            // Barra superior con título y acceso rápido al carrito.
            TopAppBar(
                title = { Text("Catálogo", fontWeight = FontWeight.SemiBold) },
                actions = {
                    TextButton(onClick = { navController.navigate(com.example.usagi_tienda_app.Routes.CART) }) {
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
            Text(
                text = "Explora los productos por categoría",
                style = MaterialTheme.typography.titleMedium
            )

            // Fila de filtros de categoría.
            CategoryFilterRow(selectedCategory = selectedCategory, onSelected = { selectedCategory = it })

            // Lista de productos filtrados. Al hacer click, vamos al detalle.
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                filtered.forEach { fig ->
                    FigureCard(fig = fig, onClick = { navController.navigate("${Routes.DETAIL}/${fig.id}") })
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: FavoriteCategory?,
    onSelected: (FavoriteCategory?) -> Unit
) {
    // Filtros simples: Todos, Chiikawa, Peluches, Llaveros, Accesorios.
    FlowRow(horizontalGap = 8.dp, verticalGap = 8.dp) {
        FilterChip(label = "Todos", selected = selectedCategory == null) { onSelected(null) }
        FilterChip(label = "Chiikawa", selected = selectedCategory == FavoriteCategory.CHIIKAWA) { onSelected(FavoriteCategory.CHIIKAWA) }
        FilterChip(label = "Peluches", selected = selectedCategory == FavoriteCategory.PELUCHES) { onSelected(FavoriteCategory.PELUCHES) }
        FilterChip(label = "Llaveros", selected = selectedCategory == FavoriteCategory.LLAVEROS) { onSelected(FavoriteCategory.LLAVEROS) }
        FilterChip(label = "Accesorios", selected = selectedCategory == FavoriteCategory.ACCESORIOS) { onSelected(FavoriteCategory.ACCESORIOS) }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    // Chip básico: cambia color según si está seleccionado.
    OutlinedButton(onClick = onClick, colors = ButtonDefaults.outlinedButtonColors()) {
        Text(if (selected) "✓ $label" else label)
    }
}

@Composable
private fun FigureCard(fig: Figure, onClick: () -> Unit) {
    // Tarjeta simple que muestra nombre, precio y categoría.
    Card {
        Column(modifier = Modifier.padding(12.dp).clickable { onClick() }) {
            Text(fig.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "$${fig.price}", style = MaterialTheme.typography.bodyMedium)
            Text(text = categoryLabel(fig.category), style = MaterialTheme.typography.labelMedium)
        }
    }
}

private fun categoryLabel(cat: FavoriteCategory): String = when (cat) {
    FavoriteCategory.CHIIKAWA -> "Chiikawa"
    FavoriteCategory.PELUCHES -> "Peluches"
    FavoriteCategory.LLAVEROS -> "Llaveros"
    FavoriteCategory.ACCESORIOS -> "Accesorios"
}

// Implementación simple de FlowRow usando Row con espaciado.
// NOTA: Esto no es un FlowRow real; sirve para distribuir chips con espacio horizontal.
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Dp = 0.dp,
    verticalGap: Dp = 0.dp,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
    ) {
        content()
    }
}

@Preview(name = "Catalog - Light", showBackground = true)
@Composable
fun CatalogScreenPreviewLight() {
    UsagiTheme(darkTheme = false) {
        val navController = rememberNavController()
        CatalogScreen(navController = navController)
    }
}

@Preview(name = "Catalog - Dark", showBackground = true)
@Composable
fun CatalogScreenPreviewDark() {
    UsagiTheme(darkTheme = true) {
        val navController = rememberNavController()
        CatalogScreen(navController = navController)
    }
}