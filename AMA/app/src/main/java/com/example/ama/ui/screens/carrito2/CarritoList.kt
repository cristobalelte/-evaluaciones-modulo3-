package com.example.ama.ui.screens.carrito2

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ama.data.viewmodel.CarritoViewModel
import com.example.ama.data.viewmodel.CarritoViewModelFactory
import com.example.ama.ui.screens.products.ProductCard

@Composable
fun CarritoList(modifier: Modifier = Modifier) {
    val vm: CarritoViewModel = viewModel(factory = CarritoViewModelFactory())

    val carritoList by vm.carritoList.collectAsState()

    LazyColumn(modifier = modifier) {
        items(carritoList.size) { index ->
            ProductCard(product = carritoList[index])
        }
    }
}
