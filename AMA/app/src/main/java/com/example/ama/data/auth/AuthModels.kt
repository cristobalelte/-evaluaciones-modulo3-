package com.example.ama.data.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.LoginRequest
import com.example.ama.data.local.UserPrefs
import com.example.ama.data.network.AuthApi
import com.example.ama.core.dto.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
class AuthViewModel(
    private val authApi: AuthApi,
    private val userPrefs: UserPrefs
) : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val res = authApi.login(LoginRequest(email, password))
                userPrefs.saveAuth(token = res.token, userId = null, email = email)
                // navegar / actualizar estado
            } catch (e: HttpException) {
                val body = e.response()?.errorBody()?.string()
                Log.e("AUTH", "HTTP ${e.code()} body=$body", e)
                // estado UI: errorMessage = body ?: "Error"
            } catch (e: Exception) {
                Log.e("AUTH", "Error inesperado", e)
            }
        }
    }
}
