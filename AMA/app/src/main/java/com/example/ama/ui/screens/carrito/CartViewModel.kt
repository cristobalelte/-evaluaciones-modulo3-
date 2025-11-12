package com.example.ama.ui.screens.carrito

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.data.CartRepository
import com.example.ama.ui.components.Product // <- lo usamos en addToCart
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CartRepository(app)

    // ---- UI models ----
    data class UiProduct(
        val id: String,
        val name: String,
        val price: Double,
        val imageUrl: String?
    )
    data class CartItem(
        val product: UiProduct,   // <- usar UiProduct aquí
        val qty: Int
    )

    // Base Room stream
    val rows = repo.rows.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    // Lo que usa la UI del carrito
    val cartItems: StateFlow<List<CartItem>> = rows
        .map { list ->
            list.map { r ->
                CartItem(
                    product = UiProduct(
                        id       = r.productId,
                        name     = r.name ?: "",
                        price    = r.price ?: 0.0,
                        imageUrl = r.imageUrl
                    ),
                    qty = r.qty
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val cartCount: StateFlow<Int> = cartItems
        .map { list -> list.sumOf { it.qty } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    fun cartTotal(): Double =
        cartItems.value.sumOf { it.product.price * it.qty }

    // --- Sincronización ---
    fun refresh(owner: String? = null) = viewModelScope.launch { repo.refreshFromServer(owner) }

    // --- API única para agregar desde cualquier pantalla ---
    fun addToCart(p: Product) = viewModelScope.launch {
        repo.add(id = p.id, name = p.name, price = p.price, imageUrl = p.imageUrl)
    }

    // --- Mutaciones de cantidad ---
    fun inc(id: String) = viewModelScope.launch { repo.inc(id) }
    fun dec(id: String) = viewModelScope.launch { repo.dec(id) }

    // --- Otras operaciones ---
    fun remove(id: String) = viewModelScope.launch { repo.remove(id) }
    fun clear() = viewModelScope.launch { repo.clear() }

    // --- Checkout (POST /carrito) ---
    fun checkout(owner: String, onSuccess: () -> Unit, onFail: () -> Unit) =
        viewModelScope.launch {
            val ok = repo.pushCart(owner)
            if (ok) {
                repo.refreshFromServer(owner) // vuelve a leer SOLO tu carrito
                onSuccess()
            } else onFail()
        }
}
