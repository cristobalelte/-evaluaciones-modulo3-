package com.example.ama.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class ProductViewModel: ViewModel() {
    private val productRepository = ProductRepository()
    val productList = MutableStateFlow<List<ProductData>>(emptyList())

    private var nextPage = 0

    init {
        getProducts()
    }

    fun getNextProduct() {
        nextPage++
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect {
                productList.value = it
            }
        }
    }
}