package com.example.ama.ui.screens.products

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ama.data.viewmodel.ProductViewModel
import com.example.ama.data.viewmodel.ProductViewModelFactory

@Composable
fun ProductList(modifier: Modifier = Modifier) {
    val productListViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory()
    )
    val productList by productListViewModel.productList.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
//                Log.d("ProductList", "Index: ${(index?:0)+1}, Size: ${productList.size}")
                if ((index ?: 0) + 1 == productList.size) {
                    productListViewModel.getNextProduct()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(productList.size) { index ->
            ProductCard(product = productList[index])
        }

    }
}