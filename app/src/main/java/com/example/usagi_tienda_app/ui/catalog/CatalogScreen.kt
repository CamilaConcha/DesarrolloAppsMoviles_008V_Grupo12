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
import com.example.usagi_tienda_app.ui.components.UsagiTopBar
import androidx.compose.ui.res.stringResource
import com.example.usagi_tienda_app.R

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
    // chip de categoría con cambio visual al seleccionar
    // tarjeta de producto con nombre, precio y categoría
    // Distribución basada en Row con espaciado horizontal (simple y directa)
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
            // Barra superior consistente
            UsagiTopBar(
                navController = navController,
                title = stringResource(R.string.title_catalog),
                showBack = true,
                showCartAction = true
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
                text = stringResource(R.string.catalog_explore),
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
    // Defino filtros: Todos, Chiikawa, Peluches, Llaveros, Accesorios.
    FlowRow(horizontalGap = 8.dp, verticalGap = 8.dp) {
        FilterChip(label = stringResource(R.string.filter_all), selected = selectedCategory == null) { onSelected(null) }
        FilterChip(label = stringResource(R.string.filter_chiikawa), selected = selectedCategory == FavoriteCategory.CHIIKAWA) { onSelected(FavoriteCategory.CHIIKAWA) }
        FilterChip(label = stringResource(R.string.filter_peluches), selected = selectedCategory == FavoriteCategory.PELUCHES) { onSelected(FavoriteCategory.PELUCHES) }
        FilterChip(label = stringResource(R.string.filter_llaveros), selected = selectedCategory == FavoriteCategory.LLAVEROS) { onSelected(FavoriteCategory.LLAVEROS) }
        FilterChip(label = stringResource(R.string.filter_accesorios), selected = selectedCategory == FavoriteCategory.ACCESORIOS) { onSelected(FavoriteCategory.ACCESORIOS) }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    // Chip Material3 nativo para mejor visual y consistencia
    androidx.compose.material3.FilterChip(
        onClick = onClick,
        label = { Text(label) },
        selected = selected
    )
}

@Composable
private fun FigureCard(fig: Figure, onClick: () -> Unit) {
    // Tarjeta con mejor espaciado y área clicable completa
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(fig.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "$${fig.price}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
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

// Distribución de chips basada en Row con espaciado horizontal.
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
