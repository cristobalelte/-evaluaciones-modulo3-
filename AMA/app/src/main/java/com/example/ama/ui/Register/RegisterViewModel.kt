package com.example.ama.ui.Register

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController

import com.example.ama.data.auth.AuthRepository
import com.example.ama.data.auth.AuthRepositoryImpl
import com.example.ama.data.auth.RegisterRequest
import com.example.ama.data.local.UserPrefs
import com.example.ama.data.network.NetworkModule
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

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
        role = when (v.uppercase()) {
            "BUYER", "SELLER" -> v.uppercase()
            else -> "BUYER"
        }
    }

    // ---------- CAMPOS ----------
    var name by mutableStateOf("")
        private set
    fun onNameChange(v: String) { name = v; clearErrorIfValidNow() }

    var lastName by mutableStateOf("")
        private set
    fun onLastNameChange(v: String) { lastName = v; clearErrorIfValidNow() }

    var phone by mutableStateOf("")
        private set

    // Validación simple: +569XXXXXXXX o solo dígitos (8 a 15). Ajusta si tu backend exige formato exacto.
    private val phoneRegex = Regex("^\\+?\\d{8,15}$")

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
        val e = email.trim()
        emailError = when {
            e.isBlank() -> "El correo no puede estar vacío"
            !emailRegex.matches(e) -> "Por favor, ingresa bien tu correo"
            else -> null
        }
        clearErrorIfValidNow()
    }

    var password by mutableStateOf("")
        private set
    fun onPasswordChange(v: String) { password = v; clearErrorIfValidNow() }

    var confirmPassword by mutableStateOf("")
        private set
    fun onConfirmPasswordChange(v: String) { confirmPassword = v; clearErrorIfValidNow() }

    // ---------- ESTADO DE VALIDACIÓN ----------
    var emailError: String? by mutableStateOf(null)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var hasSubmitted by mutableStateOf(false)
        private set

    // ---------- FLAGS POR CAMPO ----------
    val isNameError: Boolean get() = hasSubmitted && name.isBlank()
    val isNameOk: Boolean get() = name.isNotBlank()

    val isLastNameError: Boolean get() = hasSubmitted && lastName.isBlank()
    val isLastNameOk: Boolean get() = lastName.isNotBlank()

    val isPhoneError: Boolean
        get() = hasSubmitted && (phone.trim().isBlank() || !phoneRegex.matches(phone.trim()))
    val isPhoneOk: Boolean
        get() = phoneRegex.matches(phone.trim())

    val isEmailError: Boolean get() = emailError != null && email.isNotBlank()
    val isEmailOk: Boolean get() = emailError == null && email.isNotBlank()

    // ⬇️ cambio importante: min 8
    val isPasswordError: Boolean get() = hasSubmitted && password.length < 8
    val isPasswordOk: Boolean get() = password.length >= 8

    val isConfirmPasswordError: Boolean
        get() = hasSubmitted && (confirmPassword.isBlank() || confirmPassword != password)
    val isConfirmPasswordOk: Boolean
        get() = confirmPassword.isNotBlank() && confirmPassword == password

    // ---------- VALIDACIÓN GLOBAL ----------
    val isValid: Boolean
        get() = name.trim().isNotBlank() &&
                lastName.trim().isNotBlank() &&
                phoneRegex.matches(phone.trim()) &&
                email.trim().isNotBlank() &&
                emailError == null &&
                password.length >= 8 &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword

    private fun clearErrorIfValidNow() {
        if (isValid) errorMessage = ""
    }

    private fun onSubmit() {
        hasSubmitted = true
        val e = email.trim()
        val p = phone.trim()

        errorMessage = when {
            name.trim().isBlank() -> "El nombre es obligatorio"
            lastName.trim().isBlank() -> "El apellido es obligatorio"
            p.isBlank() -> "El teléfono es obligatorio"
            !phoneRegex.matches(p) -> "Teléfono inválido (ej: +56912345678)"
            e.isBlank() -> "El correo es obligatorio"
            emailError != null -> emailError ?: "Correo inválido"
            password.isBlank() || confirmPassword.isBlank() -> "Completa ambas contraseñas"
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            password != confirmPassword -> "Las contraseñas no coinciden"
            else -> ""
        }
    }

    private fun Throwable.toPrettyError(): String {
        return when (this) {
            is HttpException -> {
                val code = this.code()
                val raw = try { this.response()?.errorBody()?.string().orEmpty() } catch (_: Exception) { "" }

                // Si viene JSON con { "message": "..." }
                val msgFromJson = try {
                    if (raw.isNotBlank()) JSONObject(raw).optString("message")
                    else ""
                } catch (_: Exception) { "" }

                when {
                    msgFromJson.isNotBlank() -> msgFromJson
                    raw.isNotBlank() -> "Error $code: $raw"
                    else -> "Error $code al registrar"
                }
            }
            else -> this.message ?: "No se pudo registrar"
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
            try {
                val req = RegisterRequest(
                    firstName = name.trim(),
                    lastName = lastName.trim(),
                    email = email.trim().lowercase(),
                    phone = phone.trim(),
                    password = password,
                    role = role // "BUYER" o "SELLER"
                )

                repo.register(req)
                    .onSuccess { resp ->
                        prefs.saveAuth(
                            token = resp.token,
                            userId = resp.id,
                            email = resp.email ?: email.trim().lowercase()
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

                        onSuccess()
                    }
                    .onFailure { e ->
                        errorMessage = e.toPrettyError()
                    }
            } finally {
                isLoading = false
            }
        }
    }
}
