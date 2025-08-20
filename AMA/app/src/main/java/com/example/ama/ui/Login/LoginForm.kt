package com.example.ama.ui.Login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onSubmit: suspend () -> Boolean,          // el contenedor decide navegación
) {
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        // Campos
        val vm = androidx.lifecycle.viewmodel.compose.viewModel<LoginViewModel>()

        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.onEmailChange(it) },
            label = { Text("Correo electrónico") },
            placeholder = { Text("ejemplo@gmail.com") },
            singleLine = true,
            isError = vm.emailError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(vm.emailError != null) {
            Text(
                vm.emailError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = vm.password,
            onValueChange = { vm.onPasswordChange(it) },
            label = { Text("Contraseña") },
            singleLine = true,
            isError = vm.passwordError != null,
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Intento de submit con tecla Done
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(vm.passwordError != null) {
            Text(
                vm.passwordError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = vm.rememberMe,
                onCheckedChange = { vm.toggleRememberMe() }
            )
            Text("Recordarme")
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { /* TODO: recuperar contraseña */ }) {
                Text("¿Olvidaste tu contraseña?")
            }
        }

        // Botón
        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                scope.launch {
                    val ok = onSubmit()
                    if (!ok) {
                        snackbarHostState.showSnackbar(
                            "Revisa los campos e intenta nuevamente."
                        )
                    }
                }
            },
            enabled = !vm.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (vm.isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text("Ingresar")
        }
    }
}