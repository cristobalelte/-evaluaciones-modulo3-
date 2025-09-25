package com.example.ama.ui.screens.AddProductScreen


import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.publish.AddProductScreen
import kotlinx.coroutines.launch


@Composable
fun AddProductRoute(
    vm: CatalogViewModel,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }

    // Asegura repos y datos listos (JSON + Room)
    LaunchedEffect(Unit) {
        vm.attachCatalog(ctx)
        vm.attachCart(ctx)
        vm.loadFromDisk(ctx)
    }

    AddProductScreen(
        snackbarHostState = snack,
        onBack = onBack,
        onPublish = { np ->
            scope.launch {
                vm.addProduct(
                    name = np.name,
                    price = np.price,
                    author = np.author,
                    region = np.region,
                    type = np.type,
                    stock = np.stock,
                    subcategory = np.subcategory,
                    description = np.description,
                    imageSrc = np.imageUri
                )
                snack.showSnackbar("Producto publicado")
                onBack()
            }
        }
    )
}

