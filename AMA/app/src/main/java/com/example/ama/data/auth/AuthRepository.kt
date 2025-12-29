package com.example.ama.data.auth

interface AuthRepository {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
}
