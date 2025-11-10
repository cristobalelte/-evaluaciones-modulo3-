package com.example.ama.ui.screens.products

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

//Ruta: ProductList
@Composable
fun ProductScreen(){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ProductList(modifier = Modifier.padding(innerPadding))
    }
}