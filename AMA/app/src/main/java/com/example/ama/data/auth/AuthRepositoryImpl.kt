package com.example.ama.data.auth

import com.example.ama.core.dto.LoginRequest
import com.example.ama.core.dto.LoginResponse
import com.example.ama.data.network.AuthApi
import com.google.gson.Gson
import retrofit2.Response

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val resp = api.register(request)
            if (resp.isSuccessful) {
                resp.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(parseError(resp) ?: "No se pudo registrar (${resp.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message ?: "intenta nuevamente"}"))
        }
    }

    override suspend fun login(req: LoginRequest): Result<LoginResponse> {
        return try {
            val resp = api.login(req)
            if (resp.isSuccessful) {
                resp.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(parseError(resp) ?: "No se pudo iniciar sesión (${resp.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message ?: "intenta nuevamente"}"))
        }
    }

    private fun parseError(resp: Response<*>): String? {
        return try {
            val raw = resp.errorBody()?.string() ?: return null
            Gson().fromJson(raw, ApiError::class.java).message
        } catch (_: Exception) {
            null
        }
    }
}
