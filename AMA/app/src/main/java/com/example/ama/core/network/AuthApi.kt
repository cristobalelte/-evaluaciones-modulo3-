package com.example.ama.data.network

import com.example.ama.core.dto.LoginRequest
import com.example.ama.core.dto.LoginResponse
import com.example.ama.data.auth.RegisterRequest
import com.example.ama.data.auth.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse


    @POST("auth/register")
    suspend fun register(@Body req: RegisterRequest): RegisterResponse
}
