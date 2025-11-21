package com.example.ama.data.repository

import com.example.ama.data.dataclass.EquipoAmaItem
import com.example.ama.data.services.BackEndApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EquipoRepository {

    private val backEndApiService = BackEndApiService.RetrofitInstance.api

    //    val products: Flow<List<ProductData>> = dao.getAll()
    private val limit = 10
    private val offset = 0

    // Funciones que solo interactúan con la API Service externa
//    Esta fun, recibe todos los productos pero desde la Nube
    fun getEquipo(): Flow<List<EquipoAmaItem>> = flow {
        val response = backEndApiService.getEquipo(limit, offset)
        //Con emit se liberan los datos:
        emit(response)
    }
}