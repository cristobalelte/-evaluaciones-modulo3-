package com.example.ama.data.repository

import com.example.ama.data.dataclass.ProductData
import com.example.ama.data.services.BackEndApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CarritoRepository {
    private val backEndApiService = BackEndApiService.RetrofitInstance.api

    //    val products: Flow<List<ProductData>> = dao.getAll()
    private val limit = 3
    private val offset = 0

    // Funciones que solo interactúan con la API Service externa
//    Esta fun, recibe todos los productos pero desde la Nube
    fun getCarrito(): Flow<List<ProductData>> = flow {
        val response = backEndApiService.getCarrito(limit, offset)
        //Con emit se liberan los datos:
        emit(response)
    }
}