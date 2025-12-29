package com.example.ama.data.auth

import com.example.ama.core.dto.LoginRequest
import com.example.ama.core.dto.LoginResponse

interface AuthRepository {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
    suspend fun login(req: LoginRequest): Result<LoginResponse>
}
