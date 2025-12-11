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

//    Creamos una variable que almacena los productos recolectados por el CarritoViewModel, que a su vez es tomado del CartRepository
//    y que a su vez es tomado de la API Service externa:
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

//    Se muestra en un formato ProductCard la lista de productos (carritoList) que se recolecta del CarritoViewModel:
    LazyColumn(
        state = listState,
        modifier = modifier
    ){
        items(carritoList.size) { index ->
            ProductCard(product = carritoList[index])
        }

    }



}