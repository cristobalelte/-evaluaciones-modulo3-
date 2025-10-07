package com.example.ama.ui.Register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {
    var name by mutableStateOf("")
        private set
    fun onNameChange(v: String) { name = v }

    var lastName by mutableStateOf("")
        private set
    fun onLastNameChange(v: String) { lastName = v }

    var phone by mutableStateOf("")
        private set
    fun onPhoneChange(v: String) { phone = v }

    var email by mutableStateOf("")
        private set
    fun onEmailChange(v: String) {
        email = v
        validateEmail(v) // 👈 validación inmediata al escribir
    }

    var password by mutableStateOf("")
        private set
    fun onPasswordChange(v: String) { password = v }

    var confirmPassword by mutableStateOf("")
        private set
    fun onConfirmPasswordChange(v: String) { confirmPassword = v }

    var emailError: String? by mutableStateOf(null)

    // Valida correo */
    private fun validateEmail(value: String) {
        emailError =
            if (value.isBlank()) "El correo no puede estar vacío"
            else if (!Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$").matches(value))
                "Debe ser un correo Gmail válido (ejemplo@gmail.com)"
            else null
    }


}

