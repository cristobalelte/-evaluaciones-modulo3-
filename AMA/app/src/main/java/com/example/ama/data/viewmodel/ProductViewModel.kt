package com.example.ama.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.data.dataclass.ProductData
import com.example.ama.data.repository.ProductRepository
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