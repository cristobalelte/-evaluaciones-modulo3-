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
        return try {
            Result.success(api.register(request))
        } catch (e: HttpException) {
            Result.failure(Exception(parseHttpError(e) ?: "No se pudo registrar (${e.code()})"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión. Intenta nuevamente."))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error inesperado"))
        }
    }

    override suspend fun login(req: LoginRequest): Result<LoginResponse> {
        return try {
            Result.success(api.login(req))
        } catch (e: HttpException) {
            Result.failure(Exception(parseHttpError(e) ?: "Credenciales inválidas (${e.code()})"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión. Intenta nuevamente."))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error inesperado"))
        }
    }

    private fun parseHttpError(e: HttpException): String? {
        return try {
            val raw = e.response()?.errorBody()?.string() ?: return null
            Gson().fromJson(raw, ApiError::class.java)?.message
        } catch (_: Exception) {
            null
        }
    }
}
