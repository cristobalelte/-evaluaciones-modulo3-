package com.example.ama.ui.Register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class RegisterViewModel : ViewModel() {

    var navController: NavController? = null

    // ---------- CAMPOS ----------
    var name by mutableStateOf("")
        private set
    fun onNameChange(v: String) {
        name = v
        clearErrorIfValidNow()
    }

    var lastName by mutableStateOf("")
        private set
    fun onLastNameChange(v: String) {
        lastName = v
        clearErrorIfValidNow()
    }

    var phone by mutableStateOf("")
        private set
    fun onPhoneChange(v: String) {
        phone = v
        clearErrorIfValidNow()
    }

    var region by mutableStateOf("")
        private set
    fun onRegionChange(v: String) { region = v }

    var city by mutableStateOf("")
        private set
    fun onCityChange(v: String) { city = v }

    var address by mutableStateOf("")
        private set
    fun onAddressChange(v: String) { address = v }

    var email by mutableStateOf("")
        private set
    private val emailRegex =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun onEmailChange(v: String) {
        email = v
        // validación EN TIEMPO REAL para el color verde/rojo
        emailError = when {
            email.isBlank() -> "El correo no puede estar vacío"
            !emailRegex.matches(email) -> "Por favor, ingresa bien tu correo"
            else -> null
        }
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

    // ---------- ESTADO DE VALIDACIÓN ----------
    var emailError: String? by mutableStateOf(null)
        private set

    var errorMessage by mutableStateOf("")
        private set

    // ¿Ya se apretó el botón al menos una vez?
    var hasSubmitted by mutableStateOf(false)
        private set

    // ---------- FLAGS POR CAMPO (para bordes e íconos) ----------

    // Nombre
    val isNameError: Boolean
        get() = hasSubmitted && name.isBlank()

    val isNameOk: Boolean
        get() = name.isNotBlank()

    // Apellido
    val isLastNameError: Boolean
        get() = hasSubmitted && lastName.isBlank()

    val isLastNameOk: Boolean
        get() = lastName.isNotBlank()

    // Email
    val isEmailError: Boolean
        get() = emailError != null && email.isNotBlank()

    val isEmailOk: Boolean
        get() = emailError == null && email.isNotBlank()

    // Contraseña (mínimo 6 caracteres)
    val isPasswordError: Boolean
        get() = hasSubmitted && password.length < 6

    val isPasswordOk: Boolean
        get() = password.length >= 6

    // Confirmar contraseña
    val isConfirmPasswordError: Boolean
        get() = hasSubmitted &&
                (confirmPassword.isBlank() || confirmPassword != password)

    val isConfirmPasswordOk: Boolean
        get() = confirmPassword.isNotBlank() && confirmPassword == password

    // ---------- VALIDACIÓN GLOBAL ----------
    val isValid: Boolean
        get() = name.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank() &&
                emailError == null &&
                password.length >= 6 &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword

    private fun clearErrorIfValidNow() {
        if (isValid) {
            errorMessage = ""
        }
    }

    fun onSubmit() {
        hasSubmitted = true

        // mensaje general
        errorMessage = when {
            name.isBlank() -> "El nombre es obligatorio"
            lastName.isBlank() -> "El apellido es obligatorio"
            email.isBlank() -> "El correo es obligatorio"
            emailError != null -> emailError ?: "Correo inválido"
            password.isBlank() || confirmPassword.isBlank() ->
                "Completa ambas contraseñas"
            password.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres"
            password != confirmPassword ->
                "Las contraseñas no coinciden"
            else -> ""
        }
    }
}
