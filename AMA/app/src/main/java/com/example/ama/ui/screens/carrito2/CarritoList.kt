package com.example.ama.ui.screens.carrito2

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ama.data.viewmodel.CarritoViewModel
import com.example.ama.data.viewmodel.CarritoViewModelFactory
import com.example.ama.ui.screens.products.ProductCard

@Composable
fun CarritoList(modifier: Modifier = Modifier){
    val carritoListViewModel: CarritoViewModel = viewModel(
        factory = CarritoViewModelFactory()
    )

    val carritoList by carritoListViewModel.carritoList.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
//                Log.d("ProductList", "Index: ${(index?:0)+1}, Size: ${productList.size}")
                if ((index?:0)+1 == carritoList.size) {
                    carritoListViewModel.getNextProduct()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
    ){
        items(carritoList.size) { index ->
            ProductCard(product = carritoList[index])
        }

    }



}