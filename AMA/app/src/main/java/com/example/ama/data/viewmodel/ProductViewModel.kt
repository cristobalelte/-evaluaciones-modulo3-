package com.example.ama.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.ProductDto
import com.example.ama.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val productRepository = ProductRepository()

    val productList = MutableStateFlow<List<ProductDto>>(emptyList())

    private var nextPage = 1
    private val limit = 10

    init {
        getProducts()
    }

    fun getNextProduct() {
        nextPage++
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts(page = nextPage, limit = limit).collect { list ->
                productList.value = list
            }
        }
    }
}
