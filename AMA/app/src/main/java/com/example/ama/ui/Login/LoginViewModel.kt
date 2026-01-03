package com.example.ama.ui.Login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ama.core.dto.LoginRequest

import com.example.ama.data.auth.AuthRepository
import com.example.ama.data.auth.AuthRepositoryImpl
import com.example.ama.data.local.UserPrefs
import com.example.ama.data.network.NetworkModule

class LoginViewModel : ViewModel() {

    private val repo: AuthRepository = AuthRepositoryImpl(NetworkModule.authApi)

    var email by mutableStateOf("")
        private set
    fun onEmailChange(v: String) { email = v }

    var password by mutableStateOf("")
        private set
    fun onPasswordChange(v: String) { password = v }

    var rememberMe by mutableStateOf(false)
        private set
    fun onRememberMeChange(v: Boolean) { rememberMe = v }

    var errorMessage by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    private fun cleanEmail(raw: String): String =
        raw.trim().trim('"').lowercase() // ✅ por tu caso: vict...com"

    fun validate(): Boolean {
        val e = cleanEmail(email)
        if (e.isBlank()) { errorMessage = "El correo es obligatorio"; return false }
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
            val e = cleanEmail(email)
            val result = repo.login(LoginRequest(e, password))

            result
                .onSuccess { resp ->
                    prefs.saveAuth(
                        token = resp.token,
                        userId = resp.id,
                        email = (resp.email ?: e)
                    )
                    // si quieres “recordarme”, acá podrías guardar un flag
                    // prefs.setRememberMe(rememberMe)
                }
                .onFailure { err ->
                    errorMessage = err.message ?: "No se pudo iniciar sesión"
                }
                .isSuccess
        } finally {
            isLoading = false
        }
    }
}
