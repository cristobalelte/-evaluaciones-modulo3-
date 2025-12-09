package com.example.ama.ui.Register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class RegisterViewModel : ViewModel() {

    // Opcional: si quieres navegar desde el VM
    var navController: NavController? = null

    // ---------------- CAMPOS DE FORMULARIO ----------------

    var name by mutableStateOf("")
        private set
    fun onNameChange(v: String) { name = v }

    var lastName by mutableStateOf("")
        private set
    fun onLastNameChange(v: String) { lastName = v }

    var phone by mutableStateOf("")
        private set
    fun onPhoneChange(v: String) { phone = v }

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
    fun onEmailChange(v: String) {
        email = v
        // si está corrigiendo el correo, limpiamos el error
        emailError = null
        if (errorMessage.isNotBlank()) errorMessage = ""
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

    // ---------------- ESTADO DE ERRORES ----------------

    var emailError: String? by mutableStateOf(null)
        private set

    // Mensaje general que se muestra bajo el botón
    var errorMessage by mutableStateOf("")
        private set

    // ---------------- VALIDACIÓN GLOBAL ----------------

    val isValid: Boolean
        get() = name.isNotBlank() &&
                lastName.isNotBlank() &&
                phone.isNotBlank() &&
                region.isNotBlank() &&
                city.isNotBlank() &&
                address.isNotBlank() &&
                email.isNotBlank() &&
                emailError == null &&          // correo válido
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword

    // Valida SOLO el correo (usado antes de hacer submit)
    fun validateEmailNow() {
        emailError =
            if (email.isBlank()) {
                "El correo no puede estar vacío"
            } else if (!Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$").matches(email)) {
                "Debe ser un correo Gmail válido (ejemplo@gmail.com)"
            } else {
                null
            }
    }

    // Limpia error general si todo quedó OK
    private fun clearErrorIfValidNow() {
        if (isValid) errorMessage = ""
    }


    fun onSubmit() {
        // Siempre validar el correo al hacer submit
        validateEmailNow()

        errorMessage = when {
            name.isBlank() -> "El nombre es obligatorio"
            lastName.isBlank() -> "El apellido es obligatorio"
            phone.isBlank() -> "El teléfono es obligatorio"
            region.isBlank() -> "La región es obligatoria"
            city.isBlank() -> "La ciudad/comuna es obligatoria"
            address.isBlank() -> "La dirección es obligatoria"
            email.isBlank() -> "El correo es obligatorio"
            emailError != null -> emailError ?: "Correo inválido"
            password.isBlank() || confirmPassword.isBlank() ->
                "Completa ambas contraseñas"
            password != confirmPassword ->
                "Las contraseñas no coinciden"
            else -> {
                // Aquí podrías navegar si quieres:
                // navController?.navigate("rolScreen")
                ""
            }
        }
    }
}
