package com.example.ama.ui.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
        private set
    fun onEmailChange(v: String) {
        email = v
        validateEmail(v) // 👈 validación inmediata al escribir
    }

    var password by mutableStateOf("")
        private set
    fun onPasswordChange(v: String) { password = v }

    var rememberMe by mutableStateOf(false)
        private set
    fun toggleRememberMe() { rememberMe = !rememberMe }

    var emailError: String? by mutableStateOf(null)
    var passwordError: String? by mutableStateOf(null)

    var isLoading by mutableStateOf(false)
        private set

    /** 🔎 Valida SOLO el correo */
    private fun validateEmail(value: String) {
        emailError =
            if (value.isBlank()) "El correo no puede estar vacío"
            else if (!Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$").matches(value))
                "Debe ser un correo Gmail válido (ejemplo@gmail.com)"
            else null
    }

    /** 🔎 Valida TODO (correo + contraseña) */
    fun validate(): Boolean {
        validateEmail(email)

        passwordError =
            if (password.isBlank()) "La contraseña no puede estar vacía"
            else if (password.length < 6) "Mínimo 6 caracteres"
            else null

        return emailError == null && passwordError == null
    }

    /**
     * Simulación de login. En un proyecto real, llama a tu repo/servicio.
     * Retorna `true` si el login “resultó”.
     */
    suspend fun login(): Boolean {
        if (!validate()) return false
        isLoading = true
        delay(800) // Simula red
        isLoading = false
        return true
    }
}