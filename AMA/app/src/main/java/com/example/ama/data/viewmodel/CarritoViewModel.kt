package com.example.ama.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.ProductDto
import com.example.ama.data.repository.CarritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CarritoViewModel : ViewModel() {

    private val carritoRepository = CarritoRepository()

    val carritoList = MutableStateFlow<List<ProductDto>>(emptyList())

    init {
        getCarrito()
    }

    fun getCarrito(owner: String? = null) {
        viewModelScope.launch {
            carritoRepository.getCarrito(owner).collect { productos ->
                carritoList.value = productos
            }
        }
    }
}

