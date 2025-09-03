package com.example.ama.ui.screens.AddProductScreen


import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ama.ui.screens.publish.AddProductScreen
import kotlinx.coroutines.launch

@Composable
fun AddProductRoute(
    onBack: () -> Unit,
    onPublished: () -> Unit = {}
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    AddProductScreen(
        snackbarHostState = snackbar,
        onBack = onBack,
        onPublish = { newProd ->
            // TODO: sube la imagen (newProd.imageUri) a tu backend/Storage si lo necesitas.
            scope.launch { snackbar.showSnackbar("Producto publicado") }
            onPublished()
            onBack()
        }
    )
}

