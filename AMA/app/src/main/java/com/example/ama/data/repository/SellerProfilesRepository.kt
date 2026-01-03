package com.example.ama.data.repository

import com.example.ama.core.dto.SellerProfileDto
import com.example.ama.core.network.ApiService

class SellerProfilesRepository(
    private val api: ApiService
) {
    suspend fun getAll(): List<SellerProfileDto> = api.getSellerProfiles()
}
