package com.example.ama.ui.screens.catalog

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CatalogViewModel : ViewModel() {

    // ---- Datos mock ----
    private val all: List<Product> = listOf(
        Product("1", "Bufanda de lana tejida a mano", 15000.0, "", "Juana Pérez",  true, 3),
        Product("2", "Juego de cerámica pintado a mano", 25000.0, "", "Cristóbal Elte", true, 1),
        Product("3", "Canasto mimbre",                   18000.0, "", "Fernando Rojas", false, 5),
        Product("4", "Chaleco tejido",                   22000.0, "", "Rosa Muñoz",    true, 0)
    )

    fun getById(id: String): Product? = all.firstOrNull { it.id == id }

    // ---- Carrito ----
    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount

    fun addToCart(p: Product) {
        _cartCount.value = _cartCount.value + 1
    }

    // ---- Filtro “solo disponibles” ----
    private val _onlyAvailable = MutableStateFlow(true)
    val onlyAvailable: StateFlow<Boolean> = _onlyAvailable

    fun setOnlyAvailable(value: Boolean) {
        _onlyAvailable.value = value
        refresh()
    }

    // ---- Paginación simple ----
    private val pageSize = 10
    private var nextIndex = 0

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private fun currentFiltered(): List<Product> =
        if (onlyAvailable.value) all.filter { it.isActive && it.stock > 0 } else all

    fun refresh() {
        _products.value = emptyList()
        nextIndex = 0
        loadMore()
    }

    fun loadMore() {
        val filtered = currentFiltered()
        if (nextIndex >= filtered.size) return
        val end = minOf(nextIndex + pageSize, filtered.size)
        _products.value = _products.value + filtered.subList(nextIndex, end)
        nextIndex = end
    }
}

