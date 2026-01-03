package com.example.ama.data.repository

import com.example.ama.core.dto.CategoryDto
import com.example.ama.data.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository {
    private val api = NetworkModule.apiService

    suspend fun getCategories(): List<CategoryDto> = withContext(Dispatchers.IO) {
        api.getCategories()
            .filter { it.isActive != false } // solo activos
            .sortedBy { it.name?.lowercase() }
    }
}
