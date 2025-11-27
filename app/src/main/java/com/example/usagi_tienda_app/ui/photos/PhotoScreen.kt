package com.example.usagi_tienda_app.ui.photos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.usagi_tienda_app.presentation.photos.PhotosViewModel
import com.example.usagi_tienda_app.ui.components.UsagiTopBar
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.example.usagi_tienda_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(navController: NavController) {
    val vm = remember { PhotosViewModel() }
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(topBar = {
        UsagiTopBar(
            navController = navController,
            title = stringResource(R.string.title_photos),
            showBack = true
        )
    }) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.photos) { photo ->
                            Card {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val req = ImageRequest.Builder(LocalContext.current)
                                        .data(photo.thumbnailUrl)
                                        .memoryCachePolicy(CachePolicy.DISABLED)
                                        .diskCachePolicy(CachePolicy.DISABLED)
                                        .build()
                                    AsyncImage(
                                        model = req,
                                        contentDescription = photo.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                            .clip(MaterialTheme.shapes.medium),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(photo.title, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
