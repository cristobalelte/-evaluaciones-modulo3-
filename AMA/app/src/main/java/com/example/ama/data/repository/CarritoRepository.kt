package com.example.ama.data.repository

import android.util.Log
import com.example.ama.core.dto.ProductDto
import com.example.ama.core.network.ApiService
import com.example.ama.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class CarritoRepository(
    private val api: ApiService = NetworkModule.apiService
) {

    /**
     * Devuelve los productos del carrito elegido.
     * - Si owner != null: intenta encontrar carrito de ese owner.
     * - Si owner == null: toma el carrito más reciente por createdAt.
     */
    fun getCarrito(owner: String? = null): Flow<List<ProductDto>> = flow {
        try {
            val carts = api.getShoppingCarList(owner)

            val chosen = if (owner != null) {
                carts.firstOrNull { it.owner.equals(owner, ignoreCase = true) }
            } else {
                carts.maxByOrNull { it.createdAt ?: "" }
            }

            emit(chosen?.productos.orEmpty())

        } catch (e: HttpException) {
            val body = e.response()?.errorBody()?.string()
            Log.e("CARRITO", "HTTP ${e.code()} body=$body", e)
            emit(emptyList())
        } catch (e: Exception) {
            Log.e("CARRITO", "Error", e)
            emit(emptyList())
        }
    }
}
