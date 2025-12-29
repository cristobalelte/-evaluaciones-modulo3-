package com.example.ama.data.auth

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String // "BUYER" o "SELLER"
)

data class RegisterResponse(
    val id: String? = null,
    val email: String? = null,
    val token: String? = null
)

data class ApiError(
    val message: String? = null
)
