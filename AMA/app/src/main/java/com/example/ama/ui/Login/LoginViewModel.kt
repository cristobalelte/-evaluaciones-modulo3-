package com.example.ama.ui.Login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ama.core.dto.LoginRequest
import com.example.ama.core.network.NetworkModule
import com.example.ama.data.auth.AuthRepository
import com.example.ama.data.auth.AuthRepositoryImpl
import com.example.ama.data.local.UserPrefs
import kotlinx.coroutines.delay

class LoginViewModel : ViewModel() {

    private val repo: AuthRepository = AuthRepositoryImpl(NetworkModule.authApi)

    var email by mutableStateOf("")
        private set
    fun onEmailChange(v: String) { email = v }
    var rememberMe by mutableStateOf(false)
        private set
    fun onRememberMeChange(v: Boolean) {
        rememberMe = v
    }
    var password by mutableStateOf("")
        private set
    fun onPasswordChange(v: String) { password = v }

    var errorMessage by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun validate(): Boolean {
        if (email.isBlank()) { errorMessage = "El correo es obligatorio"; return false }
        if (password.length < 6) { errorMessage = "Mínimo 6 caracteres"; return false }
        errorMessage = ""
        return true
    }

    suspend fun login(context: Context): Boolean {
        if (!validate()) return false

        isLoading = true
        errorMessage = ""
        val prefs = UserPrefs(context)

        return try {
            val result = repo.login(LoginRequest(email.trim().lowercase(), password))

            result
                .onSuccess { resp ->
                    prefs.saveAuth(
                        token = resp.token,
                        userId = resp.id,
                        email = (resp.email ?: email.trim().lowercase())
                    )
                }
                .onFailure { e ->
                    errorMessage = e.message ?: "No se pudo iniciar sesión"
                }
                .isSuccess
        } catch (e: Exception) {
            errorMessage = e.message ?: "Error de red"
            false
        } finally {
            isLoading = false
        }
    }
}
