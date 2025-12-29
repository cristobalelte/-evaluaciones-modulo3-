package com.example.ama.ui.Register

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ama.core.network.NetworkModule
import com.example.ama.data.auth.AuthRepository
import com.example.ama.data.auth.AuthRepositoryImpl
import com.example.ama.data.auth.RegisterRequest
import com.example.ama.data.local.UserPrefs
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    var navController: NavController? = null

    // ---------- REPO / API ----------
    private val repo: AuthRepository = AuthRepositoryImpl(NetworkModule.authApi)

    // ---------- UI STATE ----------
    var isLoading by mutableStateOf(false)
        private set

    // BUYER / SELLER
    var role by mutableStateOf("BUYER")
        private set

    fun onRoleChange(v: String) {
        role = v
    }

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

    var hasSubmitted by mutableStateOf(false)
        private set

    // ---------- FLAGS POR CAMPO ----------
    val isNameError: Boolean
        get() = hasSubmitted && name.isBlank()

    val isNameOk: Boolean
        get() = name.isNotBlank()

    val isLastNameError: Boolean
        get() = hasSubmitted && lastName.isBlank()

    val isLastNameOk: Boolean
        get() = lastName.isNotBlank()

    val isEmailError: Boolean
        get() = emailError != null && email.isNotBlank()

    val isEmailOk: Boolean
        get() = emailError == null && email.isNotBlank()

    val isPasswordError: Boolean
        get() = hasSubmitted && password.length < 6

    val isPasswordOk: Boolean
        get() = password.length >= 6

    val isConfirmPasswordError: Boolean
        get() = hasSubmitted && (confirmPassword.isBlank() || confirmPassword != password)

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
        if (isValid) errorMessage = ""
    }

    fun onSubmit() {
        hasSubmitted = true
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

    // ---------- REGISTER API ----------
    fun register(context: Context, onSuccess: () -> Unit) {
        onSubmit()
        if (!isValid) return

        isLoading = true
        errorMessage = ""

        val prefs = UserPrefs(context)

        viewModelScope.launch {
            val req = RegisterRequest(
                firstName = name.trim(),
                lastName = lastName.trim(),
                email = email.trim(),
                phone = phone.trim(),
                password = password,
                role = role
            )

            repo.register(req)
                .onSuccess { resp ->
                    prefs.saveAuth(
                        token = resp.token,
                        userId = resp.id,
                        email = resp.email ?: email.trim()
                    )
                    prefs.saveProfile(
                        firstName = name.trim(),
                        lastName = lastName.trim(),
                        phone = phone.trim(),
                        region = region.trim(),
                        city = city.trim(),
                        address = address.trim(),
                        role = role
                    )

                    isLoading = false
                    onSuccess()
                }
                .onFailure { e ->
                    isLoading = false
                    errorMessage = e.message ?: "No se pudo registrar"
                }
        }
    }
}
