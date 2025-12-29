package com.example.ama.data.repository

import com.example.ama.core.dto.ProductDto
import com.example.ama.core.network.NetworkModule
import com.example.ama.data.dataclass.ProductData
import com.example.ama.data.services.BackEndApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository {
    private val backEndApiService = BackEndApiService.RetrofitInstance.api

    //    val products: Flow<List<ProductData>> = dao.getAll()
    private val limit = 10
    private val offset = 0
    private val api = NetworkModule.api
    // Funciones que solo interactúan con la API Service externa
//    Esta fun, recibe todos los productos pero desde la Nube
    fun getProducts(): Flow<List<ProductData>> = flow {
        val response = backEndApiService.getProducts(limit, offset)
        //Con emit se liberan los datos:
        emit(response)
    }
    suspend fun getAllNewProducts(): List<ProductDto> {
        return api.getAllNewProducts()
    }
}