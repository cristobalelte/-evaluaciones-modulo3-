package com.example.ama.ui.Register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

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
        validateEmail(v)              // 👈 validación inmediata
        clearErrorIfValidNow()
    }

    var password by mutableStateOf("")
        private set
    fun onPasswordChange(v: String) {
        password = v
        clearErrorIfValidNow()
    }

    var confirmPassword by mutableStateOf("")
        private set
    fun onConfirmPasswordChange(v: String) {
        confirmPassword = v
        clearErrorIfValidNow()
    }

    // --- Errores / validaciones ---
    var emailError: String? by mutableStateOf(null)
        private set

    // mensaje general que tu UI muestra bajo el botón
    var errorMessage by mutableStateOf("")
        private set

    // Habilita el botón cuando todo está correcto
    val isValid: Boolean
        get() = name.isNotBlank() &&
                lastName.isNotBlank() &&
                phone.isNotBlank() &&
                emailError == null &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword

    private fun validateEmail(value: String) {
        emailError =
            if (value.isBlank()) "El correo no puede estar vacío"
            else if (!Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$").matches(value))
                "Debe ser un correo Gmail válido (ejemplo@gmail.com)"
            else null
    }

    private fun clearErrorIfValidNow() {
        if (isValid) errorMessage = ""
    }

    // Llamado por el botón "Crear cuenta"
    fun onSubmit() {
        errorMessage = when {
            name.isBlank() -> "El nombre es obligatorio"
            lastName.isBlank() -> "El apellido es obligatorio"
            phone.isBlank() -> "El teléfono es obligatorio"
            email.isBlank() -> "El correo es obligatorio"
            emailError != null -> emailError ?: "Correo inválido"
            password.isBlank() || confirmPassword.isBlank() -> "Completa ambas contraseñas"
            password != confirmPassword -> "Las contraseñas no coinciden"
            else -> {
                // Aquí iría tu lógica real de registro (API/DB).
                // Si todo OK, puedes dejar mensaje vacío o uno de éxito.
                ""
            }
        }
    }
}


