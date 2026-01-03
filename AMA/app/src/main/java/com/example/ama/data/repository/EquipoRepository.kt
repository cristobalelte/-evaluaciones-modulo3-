package com.example.ama.data.repository

import android.util.Log
import com.example.ama.core.network.ApiService
import com.example.ama.data.dataclass.EquipoAmaItem
import com.example.ama.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class EquipoRepository(
    private val api: ApiService = NetworkModule.apiService
) {
    private val limit = 10
    private val offset = 0

    fun getEquipo(): Flow<List<EquipoAmaItem>> = flow {
        try {
            val response = api.getEquipo(limit = limit, offset = offset)
            emit(response)
        } catch (e: HttpException) {
            Log.e("EQUIPO", "HTTP ${e.code()} body=${e.response()?.errorBody()?.string()}", e)
            emit(emptyList())
        } catch (e: Exception) {
            Log.e("EQUIPO", "Error", e)
            emit(emptyList())
        }
    }
}
