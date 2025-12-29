package com.example.ama.data.network

import com.example.ama.data.auth.RegisterRequest
import com.example.ama.data.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>
}
