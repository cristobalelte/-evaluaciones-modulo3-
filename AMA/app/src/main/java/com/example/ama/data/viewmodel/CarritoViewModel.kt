package com.example.ama.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.data.dataclass.ProductData
import com.example.ama.data.repository.CarritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CarritoViewModel: ViewModel() {
    private val carritoRepository = CarritoRepository()
    val carritoList = MutableStateFlow<List<ProductData>>(emptyList())

    private var nextPage = 0

    init {
        getCarrito()
    }

    fun getNextProduct() {
        nextPage++
        getCarrito()
    }

    fun getCarrito() {
        viewModelScope.launch {
            carritoRepository.getCarrito().collect {
                carritoList.value = it
            }
        }
    }

}