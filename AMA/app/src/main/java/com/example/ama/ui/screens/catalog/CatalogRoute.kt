package com.example.ama.ui.screens.catalog

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect



@Composable
fun CatalogRoute(
    onViewDetail: (String) -> Unit,
    vm: CatalogViewModel
) {
    val products   by vm.products.collectAsStateWithLifecycle()
    val cartCount  by vm.cartCount.collectAsStateWithLifecycle()
    val onlyAvail  by vm.onlyAvailable.collectAsStateWithLifecycle() // si lo usas

    LaunchedEffect(Unit) { vm.refresh() }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isGrid by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    LaunchedEffect(products.size, isGrid) {
        snapshotFlow {
            if (isGrid) gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            else listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { last ->
                if (last >= products.lastIndex - 2) vm.loadMore()
            }
    }

    CatalogScreen(
        products = products,
        cartCount = cartCount,
        snackbarHostState = snackbar,
        onAddToCart = { p -> scope.launch { vm.addToCart(p); snackbar.showSnackbar("Agregado") } },
        onViewDetail = { p -> onViewDetail(p.id) },
        isGrid = isGrid,
        onToggleLayout = { isGrid = it },
        listState = listState,
        gridState = gridState,
        onlyAvailable = onlyAvail,
        onToggleOnlyAvailable = { vm.setOnlyAvailable(it) } // si lo mantienes
    )
}
