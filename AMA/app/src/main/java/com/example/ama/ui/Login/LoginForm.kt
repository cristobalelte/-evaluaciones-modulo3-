package com.example.ama.ui.Login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.ama.R
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onSubmit: suspend () -> Boolean,
) {
    var showPassword by remember { mutableStateOf(false) }

    val vm = androidx.lifecycle.viewmodel.compose.viewModel<LoginViewModel>()

    // VALIDACIÓN LOCAL
    val isEmailValid = vm.email.isNotBlank() // si quieres, aquí puedes meter regex de correo
    val isPasswordValid = vm.password.length >= 6

    val emailHasError = !isEmailValid && vm.email.isNotBlank()
    val passwordHasError = !isPasswordValid && vm.password.isNotBlank()

    val emailBorderColor = when {
        emailHasError -> Color(0xFFF44336)
        vm.email.isNotBlank() -> Color(0xFF4CAF50)
        else -> Color(0xFFBDBDBD)
    }

    val passwordBorderColor = when {
        passwordHasError -> Color(0xFFF44336)
        vm.password.isNotBlank() -> Color(0xFF4CAF50)
        else -> Color(0xFFBDBDBD)
    }

    val isFormValid = isEmailValid && isPasswordValid

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )


        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.onEmailChange(it) },
            label = { Text("Correo electrónico") },
            placeholder = { Text("ejemplo@gmail.com") },
            singleLine = true,
            isError = emailHasError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (vm.email.isNotBlank()) {
                    if (emailHasError) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Correo inválido",
                            tint = Color(0xFFF44336)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Correo válido",
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White,
                errorIndicatorColor = Color(0xFFF44336),
                cursorColor = Color.Black
            )
        )

        AnimatedVisibility(emailHasError) {
            Text(
                "Por favor, ingresa un correo válido",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }


        OutlinedTextField(
            value = vm.password,
            onValueChange = { vm.onPasswordChange(it) },
            label = { Text("Contraseña") },
            singleLine = true,
            isError = passwordHasError,
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (vm.password.isNotBlank()) {
                        if (passwordHasError) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Contraseña inválida",
                                tint = Color(0xFFF44336)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Contraseña válida",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    }
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { /* opcional: submit */ }),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White,
                errorIndicatorColor = Color(0xFFF44336),
                cursorColor = Color.Black
            )
        )

        AnimatedVisibility(passwordHasError) {
            Text(
                "La contraseña debe tener al menos 6 caracteres",
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
                Text(
                    "¿Olvidaste tu contraseña?",
                    color = Color(0xFF9C1A1A)
                )
            }
        }


        val loginButtonColor =
            if (isFormValid && !vm.isLoading) Color(0xFFFFC107) else Color(0xFFBDBDBD)

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
            enabled = isFormValid && !vm.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = loginButtonColor,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFBDBDBD),
                disabledContentColor = Color.White
            ),
            shape = CircleShape
        ) {
            if (vm.isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text("Ingresar")
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            thickness = 1.dp,
            color = Color(0xFFDDDDDD)
        )


        CustomButton(
            modifier = Modifier.clickable { /* TODO: Google */ },
            paint = painterResource(id = R.drawable.google),
            title = "Iniciar sesión con Google",
            backgroundColor = Color.White,
            borderColor = Color(0xFFBDBDBD),
            textColor = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomButton(
            modifier = Modifier.clickable { /* TODO: Facebook */ },
            paint = painterResource(id = R.drawable.facebook),
            title = "Iniciar sesión con Facebook",
            backgroundColor = Color.White,
            borderColor = Color(0xFF1877F2),
            textColor = Color(0xFF1877F2)
        )
    }
}


// Botón redondo con icono (Google / Facebook)
@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    paint: Painter,
    title: String,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 8.dp)
            .background(backgroundColor, shape = CircleShape)
            .border(1.dp, borderColor, shape = CircleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = paint,
            contentDescription = title,
            modifier = Modifier
                .padding(start = 24.dp)
                .size(24.dp)
        )

        Text(
            text = title,
            color = textColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}
