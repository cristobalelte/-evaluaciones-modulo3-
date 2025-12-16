package com.example.ama.ui.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.navigation.Routes
import com.example.ama.ui.theme.onPrimaryLight
import com.example.ama.ui.theme.primaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    onBack: (() -> Unit)? = null,
    registerVM: RegisterViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(primaryLight)
                    .padding(horizontal = 16.dp),
            ) {
                if (onBack != null) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = onPrimaryLight
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.logo_artemayor_blanco),
                    contentDescription = "Arte Mayor",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(56.dp)
                        .clickable { navController.navigate(Routes.HOME) },
                    contentScale = ContentScale.Fit
                )
            }
        },
        containerColor = primaryLight
    ) { innerPadding ->

        val topPadding = innerPadding.calculateTopPadding()

        // “Tarjeta” blanca con bordes superiores redondeados
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Título
                Text(
                    text = "Ingresa tus datos",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black
                )

                // ===== NOMBRE (*) =====
                LabelWithAsterisk("Nombre")
                FilledPillField(
                    value = registerVM.name,
                    onValueChange = registerVM::onNameChange,
                    placeholder = "Ingresa tu nombre aquí",
                    isError = registerVM.isNameError,
                    isOk = registerVM.isNameOk,
                    showValidationIcon = true
                )

                // ===== APELLIDO (*) =====
                LabelWithAsterisk("Apellido")
                FilledPillField(
                    value = registerVM.lastName,
                    onValueChange = registerVM::onLastNameChange,
                    placeholder = "Ingresa tu apellido aquí",
                    isError = registerVM.isLastNameError,
                    isOk = registerVM.isLastNameOk,
                    showValidationIcon = true
                )

                // ===== CORREO (*) =====
                LabelWithAsterisk("Correo")
                FilledPillField(
                    value = registerVM.email,
                    onValueChange = registerVM::onEmailChange,
                    placeholder = "Ingresa tu correo aquí",
                    isError = registerVM.isEmailError,
                    isOk = registerVM.isEmailOk,
                    showValidationIcon = true,
                    // solo mostramos el texto rojo si ya se apretó el botón
                    supportingText = if (registerVM.hasSubmitted) registerVM.emailError else null,
                    keyboardType = KeyboardType.Email
                )

                // ===== TELÉFONO (sin asterisco, sin check) =====
                SimpleLabel("Teléfono de contacto")
                FilledPillField(
                    value = registerVM.phone,
                    onValueChange = registerVM::onPhoneChange,
                    placeholder = "Ingresa tu número de teléfono aquí",
                    keyboardType = KeyboardType.Phone
                )

                // ===== REGIÓN =====
                SimpleLabel("Región")
                FilledPillField(
                    value = registerVM.region,
                    onValueChange = registerVM::onRegionChange,
                    placeholder = "Ingresa tu región aquí"
                )

                // ===== CIUDAD / COMUNA =====
                SimpleLabel("Ciudad/Comuna")
                FilledPillField(
                    value = registerVM.city,
                    onValueChange = registerVM::onCityChange,
                    placeholder = "Ingresa tu ciudad o comuna aquí"
                )

                // ===== DIRECCIÓN =====
                SimpleLabel("Dirección")
                FilledPillField(
                    value = registerVM.address,
                    onValueChange = registerVM::onAddressChange,
                    placeholder = "Ingresa tu dirección aquí"
                )

                // ===== CONTRASEÑA (*) =====
                LabelWithAsterisk("Contraseña")
                var showPass by remember { mutableStateOf(false) }
                FilledPillField(
                    value = registerVM.password,
                    onValueChange = registerVM::onPasswordChange,
                    placeholder = "Ingresa tu contraseña aquí",
                    isError = registerVM.isPasswordError,
                    isOk = registerVM.isPasswordOk,
                    showValidationIcon = true,
                    visualTransformation = if (showPass) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                imageVector = if (showPass)
                                    Icons.Outlined.VisibilityOff
                                else
                                    Icons.Outlined.Visibility,
                                contentDescription = null
                            )
                        }
                    }
                )

                // ===== REPETIR CONTRASEÑA (*) =====
                LabelWithAsterisk("Volver a ingresar contraseña")
                var showPass2 by remember { mutableStateOf(false) }
                FilledPillField(
                    value = registerVM.confirmPassword,
                    onValueChange = registerVM::onConfirmPasswordChange,
                    placeholder = "Re-ingresa tu contraseña aquí",
                    isError = registerVM.isConfirmPasswordError,
                    isOk = registerVM.isConfirmPasswordOk,
                    showValidationIcon = true,
                    visualTransformation = if (showPass2) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPass2 = !showPass2 }) {
                            Icon(
                                imageVector = if (showPass2)
                                    Icons.Outlined.VisibilityOff
                                else
                                    Icons.Outlined.Visibility,
                                contentDescription = null
                            )
                        }
                    }
                )

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick = {
                        registerVM.onSubmit()
                        if (registerVM.isValid) {
                            navController.navigate("home")
                        }
                    },
                    // siempre enabled para que muestre errores al hacer click
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Registrarme")
                }

                if (registerVM.errorMessage.isNotBlank()) {
                    Text(
                        text = registerVM.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/** Etiqueta con asterisco rojo (campo obligatorio) */
@Composable
private fun LabelWithAsterisk(text: String) {
    val labelStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$text ", style = labelStyle)
        Text(text = "*", style = labelStyle, color = MaterialTheme.colorScheme.primary)
    }
}

/** Etiqueta normal SIN asterisco */
@Composable
private fun SimpleLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilledPillField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    isOk: Boolean = false,
    showValidationIcon: Boolean = false,
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable (() -> Unit))? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val colors = MaterialTheme.colorScheme

    val borderColor = when {
        isError -> Color(0xFFF44336)      // rojo
        isOk -> Color(0xFF4CAF50)         // verde
        else -> Color(0xFFDDDDDD)         // gris
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        placeholder = { Text(placeholder) },
        visualTransformation = visualTransformation,
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icono de validación (X o ✓)
                if (showValidationIcon) {
                    when {
                        isError -> Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Campo inválido",
                            tint = Color(0xFFF44336)
                        )
                        isOk -> Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Campo válido",
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }
                // Icono adicional (ojo de contraseña, etc.)
                trailingIcon?.invoke()
            }
        },
        shape = RoundedCornerShape(26.dp),
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor  = Color.White,
            errorContainerColor     = Color(0xFFFFEBEE),

            focusedBorderColor      = borderColor,
            unfocusedBorderColor    = borderColor,
            disabledBorderColor     = borderColor,
            errorBorderColor        = Color(0xFFF44336),

            focusedTextColor        = colors.onSurface,
            unfocusedTextColor      = colors.onSurface,
            disabledTextColor       = colors.onSurfaceVariant,
            errorTextColor          = colors.onSurface,

            focusedPlaceholderColor   = colors.onSurfaceVariant,
            unfocusedPlaceholderColor = colors.onSurfaceVariant,
            errorPlaceholderColor     = colors.onSurfaceVariant,

            focusedTrailingIconColor   = colors.onSurfaceVariant,
            unfocusedTrailingIconColor = colors.onSurfaceVariant,
            errorTrailingIconColor     = Color(0xFFF44336)
        )
    )
}
