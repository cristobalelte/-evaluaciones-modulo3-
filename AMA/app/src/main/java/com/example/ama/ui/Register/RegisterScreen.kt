package com.example.ama.ui.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                expandedHeight = 100.dp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryLight, //Color de fondo
                    titleContentColor = onPrimaryLight, //Color del texto
                    navigationIconContentColor = onPrimaryLight, // Color del icono de navegación
                    actionIconContentColor = onPrimaryLight
                ),
                title = {
                    Image(
                        alignment = Alignment.Center,
                        painter = painterResource(R.drawable.logo_artemayor_blanco),
                        contentDescription = "Arte Mayor",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable(onClick = { navController.navigate(Routes.HOME) })
                            .width(120.dp),
                        contentScale = ContentScale.Fit // O usa ContentScale.Fit si prefieres
                    )
                },
                navigationIcon = {
                    onBack?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ingresa tus datos",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            // NOMBRE
            LabelWithAsterisk("NOMBRE")
            FilledPillField(
                value = registerVM.name,
                onValueChange = registerVM::onNameChange,
                placeholder = "Ingresa tu nombre aquí"
            )

            // APELLIDO
            LabelWithAsterisk("APELLIDO")
            FilledPillField(
                value = registerVM.lastName,
                onValueChange = registerVM::onLastNameChange,
                placeholder = "Ingresa tu apellido aquí"
            )

            // CORREO
            LabelWithAsterisk("CORREO")
            FilledPillField(
                value = registerVM.email,
                onValueChange = registerVM::onEmailChange,
                placeholder = "Ingresa tu correo aquí",
                isError = registerVM.emailError != null,
                supportingText = registerVM.emailError,
                keyboardType = KeyboardType.Email // 👈 teclado correcto para emails
            )

            // TELÉFONO
            LabelWithAsterisk("TELÉFONO DE CONTACTO")
            FilledPillField(
                value = registerVM.phone,
                onValueChange = registerVM::onPhoneChange,
                placeholder = "Ingresa tu número de teléfono aquí"
            )

            // CONTRASEÑA
            LabelWithAsterisk("CONTRASEÑA")
            var showPass by remember { mutableStateOf(false) }
            FilledPillField(
                value = registerVM.password,
                onValueChange = registerVM::onPasswordChange,
                placeholder = "Ingresa tu contraseña aquí",
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            // CONFIRMAR CONTRASEÑA
            LabelWithAsterisk("VUELVE A INGRESAR TU CONTRASEÑA")
            var showPass2 by remember { mutableStateOf(false) }
            FilledPillField(
                value = registerVM.confirmPassword,
                onValueChange = registerVM::onConfirmPasswordChange,
                placeholder = "Ingresa nuevamente tu contraseña aquí",
                visualTransformation = if (showPass2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPass2 = !showPass2 }) {
                        Icon(
                            imageVector = if (showPass2) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(Modifier.height(4.dp))

            // Botón principal
            Button(
                onClick = { registerVM.onSubmit()
                          navController.navigate("rolScreen")
                },
                enabled = registerVM.isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("REGISTRARME")
            }

            // Error general (opcional)
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

@Composable
private fun LabelWithAsterisk(text: String) {
    val labelStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text.uppercase(), style = labelStyle)
        Text(text = " *", style = labelStyle, color = MaterialTheme.colorScheme.primary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilledPillField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable (() -> Unit))? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        placeholder = { Text(placeholder) },
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(26.dp),
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = androidx.compose.material3.TextFieldDefaults.colors(
            // contenedor “pill” relleno
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor   = MaterialTheme.colorScheme.surfaceVariant,
            errorContainerColor     = MaterialTheme.colorScheme.surfaceVariant,

            // SIN líneas ni bordes
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor     = Color.Transparent,
            disabledIndicatorColor  = Color.Transparent,

            // textos / cursor
            cursorColor             = MaterialTheme.colorScheme.onSurface,
            focusedTextColor        = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor      = MaterialTheme.colorScheme.onSurface,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    )
}
