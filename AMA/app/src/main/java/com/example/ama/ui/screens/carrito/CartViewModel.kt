package com.example.ama.ui.carrito

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ama.data.CartRepository
import com.example.ama.data.db.CartRow
import com.example.ama.ui.components.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CartRepository(app)

    val rows: StateFlow<List<CartRow>> =
        repo.rows.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

   // init { viewModelScope.launch { repo.refreshFromServer() } }


    fun add(p: Product) = viewModelScope.launch {
        repo.add(p)
    }

    fun inc(id: String) = viewModelScope.launch { repo.inc(id) }
    fun dec(id: String) = viewModelScope.launch { repo.dec(id) }
    fun remove(id: String) = viewModelScope.launch { repo.remove(id) }
    fun clear() = viewModelScope.launch { repo.clear() }

    companion object {
        fun provideFactory(app: Application) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                CartViewModel(app) as T
        }
    }
}

