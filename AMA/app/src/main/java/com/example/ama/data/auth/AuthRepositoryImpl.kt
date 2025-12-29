package com.example.ama.data.auth

import com.example.ama.core.dto.LoginRequest
import com.example.ama.core.dto.LoginResponse
import com.example.ama.data.network.AuthApi
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return safeApiCall(
            call = { api.register(request) },
            defaultError = "No se pudo registrar"
        )
    }

    override suspend fun login(req: LoginRequest): Result<LoginResponse> {
        return safeApiCall(
            call = { api.login(req) },
            defaultError = "No se pudo iniciar sesión"
        )
    }

    // ---------- Helper genérico para Response<T> ----------
    private suspend fun <T> safeApiCall(
        call: suspend () -> Response<T>,
        defaultError: String
    ): Result<T> {
        return try {
            val resp = call()

            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                val msg =
                    parseError(resp) ?: "${defaultError} (${resp.code()})"
                Result.failure(Exception(msg))
            }
        } catch (e: IOException) {
            // Sin internet / timeout / etc.
            Result.failure(Exception("Error de red: ${e.message ?: "intenta nuevamente"}"))
        } catch (e: HttpException) {
            Result.failure(Exception("${defaultError}: HTTP ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("${defaultError}: ${e.message ?: "intenta nuevamente"}"))
        }
    }

    private fun parseError(resp: Response<*>): String? {
        return try {
            val raw = resp.errorBody()?.string()?.trim().orEmpty()
            if (raw.isBlank()) return null

            // Intento 1: { "message": "...", ... }
            Gson().fromJson(raw, ApiError::class.java)?.message
                ?: raw
        } catch (_: Exception) {
            null
        }
    }
}



